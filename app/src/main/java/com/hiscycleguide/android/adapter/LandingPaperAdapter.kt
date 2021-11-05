package com.hiscycleguide.android.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.hiscycleguide.android.fragment.LandingFragment
import com.hiscycleguide.android.model.LandingModel

class LandingPaperAdapter(fragmentManager: FragmentManager, private val pages: ArrayList<LandingModel>) :
    FragmentStatePagerAdapter(fragmentManager) {
    override fun getCount(): Int {
        return pages.size
    }

    override fun getItem(position: Int): Fragment {
        return LandingFragment.newInstance(pages[position])
    }
}