package com.hiscycleguide.android.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hiscycleguide.android.R
import com.hiscycleguide.android.model.ArticleModel
import com.hiscycleguide.android.util.inflate
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

class ArticleRecyclerAdapter(
    private val context: Context,
    private val articles: ArrayList<ArticleModel>
) : RecyclerView.Adapter<ArticleRecyclerAdapter.ArticleHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleHolder {
        val inflatedView = parent.inflate(R.layout.item_article, false)
        return ArticleHolder(context, inflatedView)
    }

    override fun onBindViewHolder(holder: ArticleHolder, position: Int) {
        val item = articles[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return articles.size
    }

    class ArticleHolder(private val context: Context, v: View) : RecyclerView.ViewHolder(v) {
        private var ivArticle: ImageView? = null
        private var llMark: LinearLayout? = null
        private var ivMark: ImageView? = null
        private var tvTitle: TextView? = null
        private var tvUser: TextView? = null

        init {
            ivArticle = v.findViewById(R.id.iv_article)
            llMark = v.findViewById(R.id.ll_article_mark)
            ivMark = v.findViewById(R.id.iv_article_mark)
            tvTitle = v.findViewById(R.id.tv_article_title)
            tvUser = v.findViewById(R.id.tv_article_user)
        }

        @SuppressLint("UseCompatLoadingForDrawables")
        fun bind(model: ArticleModel) {
            var imageUrl = model.imageUrl;
            if (imageUrl.isEmpty()) imageUrl = "imageUrl"
            Picasso.get()
                .load(imageUrl)
                .into(ivArticle, object : Callback {
                    override fun onSuccess() {
                        Log.d("Picasso", "success")
                    }

                    override fun onError(e: Exception?) {
                        Log.d("Picasso", "error")
                    }
                })
            tvUser!!.text = model.byname
            tvTitle!!.text = model.title
        }
    }
}