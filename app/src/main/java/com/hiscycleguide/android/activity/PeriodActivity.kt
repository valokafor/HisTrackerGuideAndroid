package com.hiscycleguide.android.activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import com.hiscycleguide.android.R
import android.view.View.OnTouchListener
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.hiscycleguide.android.calendar.HTGCalendarPicker
import com.hiscycleguide.android.calendar.OnCalendarListener
import com.hiscycleguide.android.util.toD
import com.hiscycleguide.android.util.toWDMY
import java.util.*


@SuppressLint("ClickableViewAccessibility")
class PeriodActivity : AppCompatActivity() {

    private lateinit var etPeriod: EditText
    private lateinit var tvPeriod: TextView
    private lateinit var cvPeriod: CardView
    private lateinit var cpPeriod: HTGCalendarPicker

    private var selectedDate = Date()

    private val otl = OnTouchListener { _, _ ->
        cvPeriod.visibility = View.VISIBLE
        true
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_period)

        getContent()
    }

    private fun getContent() {
        etPeriod = findViewById(R.id.et_period_mood)
        etPeriod.setOnTouchListener(otl)
        etPeriod.setText(selectedDate.toWDMY())

        tvPeriod = findViewById(R.id.tv_period_date)
        tvPeriod.text = selectedDate.toD()

        cvPeriod = findViewById(R.id.cv_period)
        cpPeriod = findViewById(R.id.cp_period)

        setEvent()
    }

    private fun setEvent() {
        cpPeriod.setOnCalendarListener(object : OnCalendarListener {
            override fun onPicker(date: Date) {
                selectedDate = date
                etPeriod.setText(selectedDate.toWDMY())
                cvPeriod.visibility = View.GONE
                tvPeriod.text = selectedDate.toD()
            }
        })
    }

    fun onClickLogin(view: View) {
        onBackPressed()
    }

}