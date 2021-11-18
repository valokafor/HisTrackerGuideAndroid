package com.hiscycleguide.android.fragment

import android.annotation.SuppressLint
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hiscycleguide.android.R
import com.hiscycleguide.android.adapter.PredictionRecyclerAdapter
import com.hiscycleguide.android.model.PredictionModel
import com.hiscycleguide.android.util.MoodType
import com.hiscycleguide.android.util.Utils
import com.hiscycleguide.android.util.toMY
import java.util.*

class PredictionFragment : Fragment() {

    private lateinit var rvPrediction: RecyclerView
    private lateinit var tvDate: TextView

    companion object {
        fun newInstance(): PredictionFragment {
            return PredictionFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_prediction, container, false)
        getContent(view)
        return view
    }

    @SuppressLint("UseRequireInsteadOfGet")
    private fun getContent(view: View) {
        rvPrediction = view.findViewById(R.id.rv_prediction)

        val predictions = arrayListOf<PredictionModel>()

        val currentMonth = GregorianCalendar(Date().year, Date().month, 1)
        val daysInMonth = currentMonth.getActualMaximum(Calendar.DAY_OF_MONTH)

        val firstDate = Date(Date().year, Date().month, 1)
        var preStatus = Utils.getMoodType(firstDate)
        var during = 0
        var startIndex = 1
        for (i in 1..daysInMonth) {
            val selectedDate = Date(Date().year, Date().month, i)
            val currentStatus = Utils.getMoodType(selectedDate)
            if (currentStatus == preStatus) {
                during++;
            } else {
                predictions.add(PredictionModel(preStatus, startIndex, during))

                startIndex = i
                during = 1
                preStatus = currentStatus
            }
        }
        predictions.add(PredictionModel(preStatus, startIndex, during))

        rvPrediction.layoutManager = LinearLayoutManager(context!!, RecyclerView.VERTICAL, false)
        val adapter = PredictionRecyclerAdapter(context!!, predictions)
        rvPrediction.adapter = adapter

        tvDate = view.findViewById(R.id.tv_prediction_date)
        tvDate.text = Date().toMY()
    }

}