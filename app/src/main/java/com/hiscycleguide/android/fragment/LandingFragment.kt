package com.hiscycleguide.android.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.hiscycleguide.android.R
import com.hiscycleguide.android.model.LandingModel

class LandingFragment(private val page: LandingModel) : Fragment() {

    private lateinit var ivLanding: ImageView
    private lateinit var tvTitle: TextView
    private lateinit var tvDesc: TextView

    companion object {
        fun newInstance(page: LandingModel): LandingFragment {
            return LandingFragment(page)
        }
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_landing, container, false)
        getContent(view)
        return view
    }

    private fun getContent(view: View) {
        ivLanding = view.findViewById(R.id.iv_landing)
        tvTitle = view.findViewById(R.id.tv_landing_title)
        tvDesc = view.findViewById(R.id.tv_landing_desc)

        ivLanding.setImageResource(page.image)
        tvTitle.text = page.title
        tvDesc.text = page.description
    }

}