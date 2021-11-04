package com.hiscycleguide.android.fragment

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.hiscycleguide.android.R
import com.hiscycleguide.android.activity.MainActivity
import com.hiscycleguide.android.activity.SettingActivity
import com.hiscycleguide.android.model.LandingModel
import java.util.*
import kotlin.collections.ArrayList

class ProfileFragment : Fragment() {

    private lateinit var cvCalendar: CalendarView

    companion object {
        fun newInstance(): ProfileFragment {
            return ProfileFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        getContent(view)
        return view
    }

    private fun getContent(view: View) {
        val ivSetting: ImageView = view.findViewById(R.id.iv_profile_setting)
        ivSetting.setOnClickListener{
            startActivity(Intent(context, SettingActivity::class.java))
            activity?.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
    }

}