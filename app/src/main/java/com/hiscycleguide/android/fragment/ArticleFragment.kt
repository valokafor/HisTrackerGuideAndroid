package com.hiscycleguide.android.fragment

import android.annotation.SuppressLint
import android.os.Bundle
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
import com.hiscycleguide.android.R
import com.hiscycleguide.android.adapter.ArticleRecyclerAdapter
import com.hiscycleguide.android.model.ArticleModel
import com.hiscycleguide.android.provider.CenterZoomLayoutManager
import com.hiscycleguide.android.util.*
import com.hiscycleguide.android.view.ArticleTapView
import java.util.*

class ArticleFragment : Fragment() {

    private lateinit var rvArticle: RecyclerView
    private lateinit var adapter: ArticleRecyclerAdapter
    private var articleTaps = arrayListOf<ArticleTapView>()

    private lateinit var cvPrev: CardView
    private lateinit var cvNext: CardView
    private lateinit var tvDate: TextView
    private lateinit var vwStatus: View

    private var tabSelected = 0

    private var sampleData = arrayListOf(
        R.drawable.sample_woman_10,
        R.drawable.sample_woman_11,
        R.drawable.sample_woman_12,
        R.drawable.sample_woman_13,
        R.drawable.sample_woman_14,
        R.drawable.sample_woman_15,
        R.drawable.sample_woman_16,
        R.drawable.sample_woman_17,
        R.drawable.sample_woman_18,
        R.drawable.sample_woman_19,
    )

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

    @SuppressLint("UseRequireInsteadOfGet", "NotifyDataSetChanged", "UseCompatLoadingForDrawables")
    private fun getContent(view: View) {
        rvArticle = view.findViewById(R.id.rv_article_body)
        rvArticle.layoutManager =
            CenterZoomLayoutManager(context!!, LinearLayoutManager.HORIZONTAL, false)

        val articles = arrayListOf<ArticleModel>()
        for (i in 0..9) {
            articles.add(ArticleModel(sampleData[i], i % 2 == 0))
        }
        adapter = ArticleRecyclerAdapter(context!!, articles)
        rvArticle.adapter = adapter
        rvArticle.adapter!!.notifyDataSetChanged()
        rvArticle.scheduleLayoutAnimation()

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
    }

    fun updateTabs() {
        for (tapView in articleTaps) {
            if (articleTaps.indexOf(tapView) == tabSelected) {
                tapView.setCheck(true)
            } else {
                tapView.setCheck(false)
            }
        }
    }

}