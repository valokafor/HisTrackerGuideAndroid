package com.hiscycleguide.android.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.hiscycleguide.android.R
import com.hiscycleguide.android.fragment.ArticleFragment
import com.hiscycleguide.android.fragment.CalendarFragment
import com.hiscycleguide.android.fragment.PredictionFragment
import com.hiscycleguide.android.fragment.ProfileFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getContent()
    }

    private fun getContent() {
        val calendarFragment = CalendarFragment.newInstance()
        val articleFragment = ArticleFragment.newInstance()
        val predictionFragment = PredictionFragment.newInstance()
        val profileFragment = ProfileFragment.newInstance()

        setCurrentFragment(calendarFragment)
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bn_main)
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