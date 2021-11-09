package com.hiscycleguide.android.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TimePicker
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.hiscycleguide.android.R
import com.hiscycleguide.android.provider.PreferenceProvider
import com.hiscycleguide.android.view.NotifyView

@SuppressLint("ClickableViewAccessibility")
class NotificationActivity : AppCompatActivity() {
    private var notifyViews = arrayListOf<NotifyView>()
    private var timeViews = arrayListOf<NotifyView>()
    private lateinit var etTime: EditText
    private lateinit var cvTimerPicker: CardView
    private lateinit var tpTimerPicker: TimePicker

    private var notifyOptions = arrayListOf<Boolean>()
    private var timeOption = 0


    private val otl = View.OnTouchListener { _, _ ->
        if (cvTimerPicker.visibility == View.GONE) {
            cvTimerPicker.visibility = View.VISIBLE
        }
        true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)

        getContent()
    }

    private fun getContent() {
        notifyOptions.add(PreferenceProvider.getOvulationNotify())
        notifyOptions.add(PreferenceProvider.getMenstrualNotify())
        notifyOptions.add(PreferenceProvider.getLutealNotify())
        notifyOptions.add(PreferenceProvider.getFollicularNotify())

        val notifyView1: NotifyView = findViewById(R.id.nv_notify_01)
        notifyView1.setOnNotifyViewListener(object: NotifyView.OnNotifyViewListener {
            override fun onChangedMark(boolean: Boolean) {
                notifyOptions[0] = boolean
            }
        })
        val notifyView2: NotifyView = findViewById(R.id.nv_notify_02)
        notifyView2.setOnNotifyViewListener(object: NotifyView.OnNotifyViewListener {
            override fun onChangedMark(boolean: Boolean) {
                notifyOptions[1] = boolean
            }
        })
        val notifyView3: NotifyView = findViewById(R.id.nv_notify_03)
        notifyView3.setOnNotifyViewListener(object: NotifyView.OnNotifyViewListener {
            override fun onChangedMark(boolean: Boolean) {
                notifyOptions[2] = boolean
            }
        })
        val notifyView4: NotifyView = findViewById(R.id.nv_notify_04)
        notifyView4.setOnNotifyViewListener(object: NotifyView.OnNotifyViewListener {
            override fun onChangedMark(boolean: Boolean) {
                notifyOptions[3] = boolean
            }
        })

        notifyViews.add(notifyView1)
        notifyViews.add(notifyView2)
        notifyViews.add(notifyView3)
        notifyViews.add(notifyView4)

        for (i in 0..3) {
            val boolean = notifyOptions[i]
            val notifyView = notifyViews[i]
            notifyView.setCheck(boolean)
        }

        timeOption = PreferenceProvider.getNotifyTime()

        val notifyTime1: NotifyView = findViewById(R.id.nv_time_01)
        notifyTime1.setOnClickListener {
            timeOption = 0
            updateTimeOption()
        }
        val notifyTime2: NotifyView = findViewById(R.id.nv_time_02)
        notifyTime2.setOnClickListener {
            timeOption = 1
            updateTimeOption()
        }
        val notifyTime3: NotifyView = findViewById(R.id.nv_time_03)
        notifyTime3.setOnClickListener {
            timeOption = 2
            updateTimeOption()
        }
        val notifyTime4: NotifyView = findViewById(R.id.nv_time_04)
        notifyTime4.setOnClickListener {
            timeOption = 3
            updateTimeOption()
        }

        timeViews.add(notifyTime1)
        timeViews.add(notifyTime2)
        timeViews.add(notifyTime3)
        timeViews.add(notifyTime4)

        etTime = findViewById(R.id.et_notification_time)
        val alarmTime = PreferenceProvider.getAlarmTime()
        etTime.setText(alarmTime)

        cvTimerPicker = findViewById(R.id.cv_notification_time)
        tpTimerPicker = findViewById(R.id.tp_notification_time)

        var hr = alarmTime!!.split(" ")[0].split(":")[0].toInt()
        if (alarmTime.split(" ")[1] == "pm") {
            hr = hr + 12
        }
        tpTimerPicker.hour = hr
        tpTimerPicker.minute = alarmTime.split(" ")[0].split(":")[1].toInt()

        updateTimeOption()

        setEvent()
    }

    private fun updateTimeOption() {
        for (view in timeViews) {
            view.setCheck(timeViews.indexOf(view) == timeOption)
        }
        etTime.setOnTouchListener(otl)
    }

    private fun setEvent() {
        tpTimerPicker.setOnTimeChangedListener { _, h, m ->
            var hour = h
            val s: String
            when {hour == 0 -> { hour += 12
                s = "AM"
            }
                hour == 12 -> s = "PM"
                hour > 12 -> { hour -= 12
                    s = "PM"
                }
                else -> s = "AM"
            }

            val hourC = if (hour < 10) "0$hour" else hour
            val min = if (m < 10) "0$m" else m

            val msg = "$hourC:$min $s"
            etTime.setText(msg)
        }
    }

    fun onClickBack(view: View) {
        onBackPressed()
    }

    fun onClickSave(view: View) {
        if (cvTimerPicker.visibility == View.GONE) {
            PreferenceProvider.setNotifyTime(timeOption)

            PreferenceProvider.setOvulationNotify(notifyOptions[0])
            PreferenceProvider.setMenstrualNotify(notifyOptions[1])
            PreferenceProvider.setLutealNotify(notifyOptions[2])
            PreferenceProvider.setFollicularNotify(notifyOptions[3])

            val alarmTime = etTime.text.toString()
            PreferenceProvider.setAlarmTime(alarmTime)

            onBackPressed()
        }
    }

    fun onClickSaveTime(view: View) {
        if (cvTimerPicker.visibility == View.VISIBLE) {
            cvTimerPicker.visibility = View.GONE
        }
    }

    override fun onBackPressed() {
        if (cvTimerPicker.visibility == View.VISIBLE) {
            cvTimerPicker.visibility = View.GONE
        } else {
            super.onBackPressed()
        }
    }

}