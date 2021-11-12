package com.hiscycleguide.android.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hiscycleguide.android.R
import com.hiscycleguide.android.util.MoodType
import com.hiscycleguide.android.util.inflate
import java.text.SimpleDateFormat
import java.util.*

class MoodRecyclerAdapter(
    private val context: Context,
    private val moods: ArrayList<MoodType>,
    private val startIndex: Int,
    private val selectedDate: Date,
) : RecyclerView.Adapter<MoodRecyclerAdapter.ArticleHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleHolder {
        val inflatedView = parent.inflate(R.layout.item_mood, false)
        return ArticleHolder(context, inflatedView)
    }

    override fun onBindViewHolder(holder: ArticleHolder, position: Int) {
        val item = moods[position]
        holder.bind(item, position, startIndex, selectedDate)
    }

    override fun getItemCount(): Int {
        return moods.size
    }

    class ArticleHolder(private val context: Context, v: View) : RecyclerView.ViewHolder(v) {
        private var ivHeader: ImageView? = null
        private var vwTop: View? = null
        private var vwBottom: View? = null
        private var tvDate: TextView? = null

        init {
            ivHeader = v.findViewById(R.id.iv_mood_header)
            vwTop = v.findViewById(R.id.vw_mood_up)
            vwBottom = v.findViewById(R.id.vw_mood_bottom)
            tvDate = v.findViewById(R.id.tv_mood_date)
        }

        @SuppressLint(
            "UseCompatLoadingForDrawables", "SimpleDateFormat", "SetTextI18n"
        )
        fun bind(model: MoodType, position: Int, startIndex: Int, selectedDate: Date) {
            val endString = SimpleDateFormat("MMM, yyyy").format(selectedDate)
            val startString = (startIndex + position).toString()
            tvDate!!.text = "$startString $endString"
            val currentMonth = GregorianCalendar(selectedDate.year, selectedDate.month, 1)
            val daysInMonth = currentMonth.getActualMaximum(Calendar.DAY_OF_MONTH)
            if ((startIndex + position) == 1) {
                vwTop!!.visibility = View.INVISIBLE
            }
            if ((startIndex + position) == daysInMonth) {
                vwBottom!!.visibility = View.INVISIBLE
            }

            if (model == MoodType.Normal) {
                ivHeader!!.setImageResource(R.drawable.dot_sky)
            } else if (model == MoodType.Ovulation) {
                ivHeader!!.setImageResource(R.drawable.dot_yellow)
                tvDate!!.setTextColor(context.resources.getColor(R.color.textColor))
            } else {
                ivHeader!!.setImageResource(R.drawable.dot_red)
                tvDate!!.setTextColor(context.resources.getColor(R.color.textColor))
            }
        }
    }
}