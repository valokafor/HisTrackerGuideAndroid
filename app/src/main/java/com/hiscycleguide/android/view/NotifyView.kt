package com.hiscycleguide.android.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.hiscycleguide.android.R

class NotifyView : LinearLayout {
    private lateinit var ivMark: ImageView
    private lateinit var tvTitle: TextView

    private var title: String? = null
    private var isCheckBox: Boolean = true
    private var isChecked = false

    private var listener: OnNotifyViewListener? = null

    constructor(context: Context) : super(context) {
        View.inflate(context, R.layout.view_htg_notify, this)
        initCalendar(null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        View.inflate(context, R.layout.view_htg_notify, this)
        initCalendar(attrs)
    }

    private fun initCalendar(attrs: AttributeSet?) {
        ivMark = findViewById(R.id.iv_notify_mark)
        tvTitle = findViewById(R.id.tv_notify_title)

        if (attrs == null) {
            title = resources.getString(R.string.ovulation_phase)
            isCheckBox = true
            return
        }
        val a = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.NotifyView, 0, 0
        )
        title = a.getString(R.styleable.NotifyView_title)
        isCheckBox = a.getBoolean(R.styleable.NotifyView_isCheckBox, true)
    }

    private fun setEvent() {
        this.setOnClickListener {
            isChecked = !isChecked
            updateMark()
            if (listener != null) listener!!.onChangedMark(isChecked)
        }
    }

    public override fun onFinishInflate() {
        super.onFinishInflate()

        tvTitle.text = title
        setEvent()
    }

    fun setCheck(boolean: Boolean) {
        isChecked = boolean
        updateMark()
    }

    private fun updateMark() {
        if (isChecked) {
            if (isCheckBox) {
                ivMark.setImageResource(R.drawable.ic_mark)
            } else {
                ivMark.setImageResource(R.drawable.back_blue_6)
            }
        } else {
            ivMark.setImageResource(R.drawable.ic_un_check)
        }
    }

    fun setOnNotifyViewListener(listener: OnNotifyViewListener) {
        this.listener = listener
    }

    interface OnNotifyViewListener {
        fun onChangedMark(boolean: Boolean)
    }

}