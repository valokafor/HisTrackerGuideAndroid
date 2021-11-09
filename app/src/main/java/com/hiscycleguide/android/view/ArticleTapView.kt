package com.hiscycleguide.android.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.hiscycleguide.android.R

class ArticleTapView : LinearLayout {
    private lateinit var llBack: LinearLayout
    private lateinit var tvTitle: TextView

    private var title: String? = null
    private var isChecked = false

    private var listener: OnArticleViewListener? = null

    constructor(context: Context) : super(context) {
        View.inflate(context, R.layout.view_article_tap, this)
        initCalendar(null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        View.inflate(context, R.layout.view_article_tap, this)
        initCalendar(attrs)
    }

    private fun initCalendar(attrs: AttributeSet?) {
        llBack = findViewById(R.id.ll_article_back)
        tvTitle = findViewById(R.id.tv_article_title)

        if (attrs == null) {
            title = resources.getString(R.string.her_body)
            isChecked = false
            return
        }
        val a = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.ArticleTapView, 0, 0
        )
        title = a.getString(R.styleable.ArticleTapView_atv_title)
        isChecked = a.getBoolean(R.styleable.ArticleTapView_atv_isCheckBox, false)
    }

    private fun setEvent() {
        this.setOnClickListener {
            if (listener != null) listener!!.onChanged()
        }
    }

    public override fun onFinishInflate() {
        super.onFinishInflate()

        tvTitle.text = title
        update()

        setEvent()
    }

    fun setCheck(boolean: Boolean) {
        isChecked = boolean
        update()
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun update() {
        if (isChecked) {
            tvTitle.setTextColor(context!!.getColor(R.color.white))
            llBack.background = context!!.getDrawable(R.drawable.back_blue_22)
        } else {
            tvTitle.setTextColor(context!!.getColor(R.color.textColor))
            llBack.background = context!!.getDrawable(R.drawable.back_stroke_white_22)
        }
    }

    fun setOnArticleViewListener(listener: OnArticleViewListener) {
        this.listener = listener
    }

    interface OnArticleViewListener {
        fun onChanged()
    }

}