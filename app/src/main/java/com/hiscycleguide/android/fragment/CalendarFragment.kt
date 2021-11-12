package com.hiscycleguide.android.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ScrollView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.hiscycleguide.android.R
import com.hiscycleguide.android.model.LandingModel

class CalendarFragment : Fragment() {

//    private lateinit var svBody: ScrollView

    companion object {
        fun newInstance(): CalendarFragment {
            return CalendarFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_calendar, container, false)
        getContent(view)
        return view
    }

    private fun getContent(view: View) {
//        svBody = view.findViewById(R.id.sv_calendar_body)
//        svBody.invalidate()
    }

}