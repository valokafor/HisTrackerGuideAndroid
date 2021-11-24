package com.hiscycleguide.android.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.google.android.material.tabs.TabLayout
import com.hiscycleguide.android.R
import com.hiscycleguide.android.activity.auth.LoginActivity
import com.hiscycleguide.android.adapter.LandingPaperAdapter
import com.hiscycleguide.android.model.LandingModel
import com.hiscycleguide.android.provider.PreferenceProvider

class LandingActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager
    private lateinit var indicator: TabLayout
    private lateinit var pagerAdapter: LandingPaperAdapter
    private lateinit var tvNext: TextView
    private lateinit var tvSkip: TextView
    private var pageIndex = 0;

    private lateinit var titles: String
    private lateinit var details: String

    var pagerListener: OnPageChangeListener = object : OnPageChangeListener {
        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
            val lastIdx: Int = pagerAdapter.getCount() - 1
            pageIndex = position;
            if (position == lastIdx) {
                tvNext.setText(R.string.finish)
            } else {
                tvNext.setText(R.string.next)
            }
        }

        override fun onPageSelected(i: Int) {

        }

        override fun onPageScrollStateChanged(state: Int) {

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing)

        titles = intent.getStringExtra("titles")!!
        details = intent.getStringExtra("details")!!

        getContent()
    }

    private fun getContent() {
        val pages = ArrayList<LandingModel>()
        val titleList = titles.split(":")
        val detailList = details.split(":")
        Log.d("Title", titles.toString())
        Log.d("Detail", details.toString())
        pages.add(LandingModel(R.drawable.img_landing_01, titleList.get(0), detailList.get(0)))
        pages.add(LandingModel(R.drawable.img_landing_02, titleList.get(1), detailList.get(1)))
        pages.add(LandingModel(R.drawable.img_landing_03, titleList.get(2), detailList.get(2)))

        viewPager = findViewById(R.id.vp_landing)
        pagerAdapter = LandingPaperAdapter(supportFragmentManager, pages)
        viewPager.adapter = pagerAdapter
        indicator = findViewById(R.id.tl_landing)
        indicator.setupWithViewPager(viewPager, true)
        viewPager.addOnPageChangeListener(pagerListener)
        tvNext = findViewById(R.id.tv_landing_next)
        tvSkip = findViewById(R.id.tv_landing_skip)
    }

    fun onClickNext(view: View) {
        if (pageIndex < pagerAdapter.count - 1) {
            viewPager.setCurrentItem(pageIndex + 1)
        } else {
            PreferenceProvider.setFirstRunning()
            startActivity(Intent(this, LoginActivity::class.java))
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }

    }

    fun onClickSkip(view: View) {
        PreferenceProvider.setFirstRunning()
        startActivity(Intent(this, LoginActivity::class.java))
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
}