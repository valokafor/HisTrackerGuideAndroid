package com.hiscycleguide.android.fragment

import android.annotation.SuppressLint
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hiscycleguide.android.R
import com.hiscycleguide.android.adapter.PredictionRecyclerAdapter
import com.hiscycleguide.android.model.PredictionModel
import com.hiscycleguide.android.util.MoodType

class PredictionFragment : Fragment() {

    private lateinit var rvPrediction: RecyclerView

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
        predictions.add(PredictionModel(MoodType.Normal, 1, 2))
        predictions.add(PredictionModel(MoodType.Menstruation, 3, 7))
        predictions.add(PredictionModel(MoodType.Normal, 10, 7))
        predictions.add(PredictionModel(MoodType.Ovulation, 17, 7))
        predictions.add(PredictionModel(MoodType.Normal, 24, 7))

        rvPrediction.layoutManager = LinearLayoutManager(context!!, RecyclerView.VERTICAL, false)
        val adapter = PredictionRecyclerAdapter(context!!, predictions)
        rvPrediction.adapter = adapter
    }

}