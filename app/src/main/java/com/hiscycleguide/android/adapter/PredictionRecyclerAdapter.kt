package com.hiscycleguide.android.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hiscycleguide.android.R
import com.hiscycleguide.android.model.PredictionModel
import com.hiscycleguide.android.util.MoodType
import com.hiscycleguide.android.util.inflate
import java.util.*

class PredictionRecyclerAdapter(
    private val context: Context,
    private val predictions: ArrayList<PredictionModel>
) : RecyclerView.Adapter<PredictionRecyclerAdapter.ArticleHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleHolder {
        val inflatedView = parent.inflate(R.layout.item_prediction, false)
        return ArticleHolder(context, inflatedView)
    }

    override fun onBindViewHolder(holder: ArticleHolder, position: Int) {
        val item = predictions[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return predictions.size
    }

    class ArticleHolder(private val context: Context, v: View) : RecyclerView.ViewHolder(v) {
        private var rvMood: RecyclerView? = null
        private var ivLogo: ImageView? = null

        init {
            rvMood = v.findViewById(R.id.rv_prediction_cell)
            rvMood!!.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)

            ivLogo = v.findViewById(R.id.iv_prediction_logo)
        }

        @SuppressLint("UseCompatLoadingForDrawables")
        fun bind(model: PredictionModel) {
            if (model.moodType == MoodType.Ovulation) {
                ivLogo!!.visibility = View.VISIBLE
                ivLogo!!.setImageDrawable(context.getDrawable(R.drawable.ic_ovulation))
                rvMood!!.background = context.getDrawable(R.drawable.gradient_yellow_3)
            } else if (model.moodType == MoodType.Menstruation) {
                ivLogo!!.visibility = View.VISIBLE
                ivLogo!!.setImageDrawable(context.getDrawable(R.drawable.ic_menstruation))
                rvMood!!.background = context.getDrawable(R.drawable.gradient_red_3)
            } else {
                ivLogo!!.visibility = View.GONE
            }

            val moods = arrayListOf<MoodType>()
            for (i in model.startIndex until model.startIndex + model.during) moods.add(model.moodType)
            val adapter = MoodRecyclerAdapter(context, moods, model.startIndex, Date())
            rvMood!!.adapter = adapter
        }
    }
}