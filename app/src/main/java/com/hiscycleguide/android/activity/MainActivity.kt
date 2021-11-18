package com.hiscycleguide.android.activity

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.hiscycleguide.android.R
import com.hiscycleguide.android.fragment.ArticleFragment
import com.hiscycleguide.android.fragment.CalendarFragment
import com.hiscycleguide.android.fragment.PredictionFragment
import com.hiscycleguide.android.fragment.ProfileFragment
import com.hiscycleguide.android.receiver.AlarmReceiver
//import com.hiscycleguide.android.service.AlarmService


@SuppressLint("UnspecifiedImmutableFlag")
class MainActivity : AppCompatActivity() {
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getContent()
        startAlarm()
    }

    private fun startAlarm() {
        val repeatTime = 60
        val processTimer: AlarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlarmReceiver::class.java)
        val pendingIntent: PendingIntent =
            PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        processTimer.setRepeating(
            AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),
            (repeatTime * 1000).toLong(), pendingIntent
        )
    }

    private fun stopAlarm() {
        val processTimer: AlarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlarmReceiver::class.java)
        val pendingIntent: PendingIntent =
            PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        processTimer.cancel(pendingIntent)
    }

    private fun getContent() {
        val calendarFragment = CalendarFragment.newInstance()
        calendarFragment.setOnCalendarFragmentListener(object :
            CalendarFragment.OnCalendarFragmentListener {
            override fun onClickPredictions() {
                bottomNavigationView.selectedItemId = R.id.bn_prediction
            }
        })
        val articleFragment = ArticleFragment.newInstance()
        val predictionFragment = PredictionFragment.newInstance()
        val profileFragment = ProfileFragment.newInstance()

        setCurrentFragment(calendarFragment)

        bottomNavigationView = findViewById(R.id.bn_main)
        bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.bn_calendar -> setCurrentFragment(calendarFragment)
                R.id.bn_article -> setCurrentFragment(articleFragment)
                R.id.bn_prediction -> setCurrentFragment(predictionFragment)
                R.id.bn_profile -> setCurrentFragment(profileFragment)
            }
            true
        }
    }

    private fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fg_main, fragment)
            commit()
        }

    override fun onBackPressed() {

    }
}