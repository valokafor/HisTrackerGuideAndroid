package com.hiscycleguide.android.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.hiscycleguide.android.R
import com.hiscycleguide.android.fragment.ArticleFragment
import com.hiscycleguide.android.fragment.CalendarFragment
import com.hiscycleguide.android.fragment.PredictionFragment
import com.hiscycleguide.android.fragment.ProfileFragment

class SettingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        getContent()
    }

    private fun getContent() {
    }

    fun onClickLogin(view: View) {
        onBackPressed()
    }

    fun onClickPeriod(view: View) {
        startActivity(Intent(this, PeriodActivity::class.java))
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }

}