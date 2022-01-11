package com.hiscycleguide.android.fragment

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.google.android.material.snackbar.Snackbar
import com.hiscycleguide.android.R
import com.hiscycleguide.android.adapter.ArticleRecyclerAdapter
import com.hiscycleguide.android.model.ArticleModel
import com.hiscycleguide.android.provider.CenterZoomLayoutManager
import com.hiscycleguide.android.provider.FirebaseProvider
import com.hiscycleguide.android.provider.ProgressProvider
import com.hiscycleguide.android.util.MoodType
import com.hiscycleguide.android.util.Utils
import com.hiscycleguide.android.util.toMY
import com.hiscycleguide.android.util.tod
import com.hiscycleguide.android.view.ArticleTapView
import java.util.*

@SuppressLint("UseCompatLoadingForDrawables", "UseRequireInsteadOfGet", "NotifyDataSetChanged")
class ArticleFragment : Fragment() {

    private lateinit var rvArticle: RecyclerView
    private lateinit var adapter: ArticleRecyclerAdapter
    private var articleTaps = arrayListOf<ArticleTapView>()

    private lateinit var cvPrev: CardView
    private lateinit var cvNext: CardView
    private lateinit var tvDate: TextView
    private lateinit var vwStatus: View
    private lateinit var vwContent: View

    private var tabSelected = 0
    private var articles = arrayListOf<ArticleModel>()
    private var showArticles = arrayListOf<ArticleModel>()

    private lateinit var progressDialog: ProgressProvider

    companion object {
        fun newInstance(): ArticleFragment {
            return ArticleFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_article, container, false)
        getContent(view)
        return view
    }


    private fun getContent(view: View) {
        rvArticle = view.findViewById(R.id.rv_article_body)
        rvArticle.layoutManager =
            CenterZoomLayoutManager(context!!, LinearLayoutManager.HORIZONTAL, false)
        adapter = ArticleRecyclerAdapter(context!!, showArticles)
        rvArticle.adapter = adapter
        rvArticle.adapter!!.notifyDataSetChanged()
        rvArticle.scheduleLayoutAnimation()

        rvArticle.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == 0) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        Log.d("onScrollStateChanged",
                            ((recyclerView.layoutManager!!) as CenterZoomLayoutManager).findLastVisibleItemPosition()
                                .toString()
                        )
                    }
                }
            }
        })

        val snapHelper: SnapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(rvArticle)

        cvNext = view.findViewById(R.id.cv_article_next)
        cvNext.setOnClickListener {
            val index =
                (rvArticle.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
            if (index < articles.size - 1) rvArticle.smoothScrollToPosition(index + 1)
        }
        cvPrev = view.findViewById(R.id.cv_article_prev)
        cvPrev.setOnClickListener {
            val index =
                (rvArticle.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
            if (index > 0) rvArticle.smoothScrollToPosition(index - 1)
        }

        tvDate = view.findViewById(R.id.tv_article_date)
        var today = Date().tod() + "th"
        today = today + " " + Date().toMY()
        tvDate.text = today

        vwStatus = view.findViewById(R.id.vw_article_status)
        when (Utils.getMoodType(Date())) {
            MoodType.Ovulation -> vwStatus.background =
                context!!.getDrawable(R.drawable.gradient_yellow)
            MoodType.Menstruation -> vwStatus.background =
                context!!.getDrawable(R.drawable.gradient_red)
            MoodType.Normal -> vwStatus.background = context!!.getDrawable(R.drawable.gradient_sky)
        }

        val tapView1: ArticleTapView = view.findViewById(R.id.at_article_body)
        tapView1.setOnArticleViewListener(object : ArticleTapView.OnArticleViewListener {
            override fun onChanged() {
                tabSelected = 0
                updateTabs()
            }
        })
        val tapView2: ArticleTapView = view.findViewById(R.id.at_article_emotions)
        tapView2.setOnArticleViewListener(object : ArticleTapView.OnArticleViewListener {
            override fun onChanged() {
                tabSelected = 1
                updateTabs()
            }
        })
        val tapView3: ArticleTapView = view.findViewById(R.id.at_article_actions)
        tapView3.setOnArticleViewListener(object : ArticleTapView.OnArticleViewListener {
            override fun onChanged() {
                tabSelected = 2
                updateTabs()
            }
        })

        articleTaps.add(tapView1)
        articleTaps.add(tapView2)
        articleTaps.add(tapView3)

        vwContent = view.findViewById(R.id.ll_content)

        progressDialog = ProgressProvider.newInstance(context!!)

        initData()
    }

    private fun onRead() {
        Handler().postDelayed({
            //doSomethingHere()
        }, 1000)
    }

    private fun initData() {
        FirebaseProvider.getArticleFirestore()
            .get()
            .addOnSuccessListener {
                articles.clear()
                for (document in it.documents) {
                    val articleModel = document!!.toObject(ArticleModel::class.java)
//                    if (articleModel!!.status == "Published") {
//                        Log.d("Article", articleModel.toJson().toString())
//                        articles.add(articleModel)
//                    }
                    articles.add(articleModel!!)
                }
                Log.d("Articles", articles.size.toString())
                refreshList()

            }.addOnFailureListener {
                Snackbar.make(
                    vwContent,
                    it.message!!,
                    Snackbar.LENGTH_LONG
                ).show()
            }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun refreshList() {
        val tapListString = arrayOf(
            context!!.getString(R.string.her_body),
            context!!.getString(R.string.her_emotions),
            context!!.getString(R.string.your_actions),
        )
        showArticles.clear()
        for (article in articles) {
//            if (article.type.lowercase(Locale.getDefault()) == tapListString[tabSelected].lowercase(
//                    Locale.getDefault()
//                )
//            ) {
//                showArticles.add(article)
//            }
            showArticles.add(article)
        }
        adapter.notifyDataSetChanged()
    }

    fun updateTabs() {
        for (tapView in articleTaps) {
            if (articleTaps.indexOf(tapView) == tabSelected) {
                tapView.setCheck(true)
            } else {
                tapView.setCheck(false)
            }
        }
        refreshList()
    }

}