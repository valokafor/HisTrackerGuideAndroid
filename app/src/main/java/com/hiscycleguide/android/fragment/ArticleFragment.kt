package com.hiscycleguide.android.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

class ArticleFragment : Fragment() {

    private lateinit var rvArticle: RecyclerView
    private lateinit var adapter: ArticleRecyclerAdapter

    private lateinit var cvPrev: CardView
    private lateinit var cvNext: CardView

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

    @SuppressLint("UseRequireInsteadOfGet", "NotifyDataSetChanged")
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
            val index = (rvArticle.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
            if (index < articles.size - 1) rvArticle.smoothScrollToPosition(index + 1)
        }
        cvPrev = view.findViewById(R.id.cv_article_prev)
        cvPrev.setOnClickListener {
            val index = (rvArticle.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
            if (index > 0) rvArticle.smoothScrollToPosition(index - 1)
        }
    }

}