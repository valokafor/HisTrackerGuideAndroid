package com.hiscycleguide.android.calendar

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import com.hiscycleguide.android.R
import java.text.SimpleDateFormat
import java.util.*
import com.hiscycleguide.android.util.diffDate
import com.hiscycleguide.android.util.toMDY


class HTGCalendarView : LinearLayout {

    private lateinit var ivLeft: ImageView
    private lateinit var ivRight: ImageView
    private lateinit var tvTitle: TextView
    private lateinit var gvCalendar: GridView

    private var title: String? = null
    private var fontSize: Float? = 14.0f
    private var textColor: Int? = Color.BLACK
    private var btnSize: Float? = 24.0f
    private var btnColor: Int? = Color.BLUE

    private var selectedDate: Date = Date()
    private var actionDate: Date = Date()

    private var listener: OnCalendarListener? = null

    fun setOnCalendarListener(listener: OnCalendarListener) {
        this.listener = listener
    }

    constructor(context: Context) : super(context) {
        View.inflate(context, R.layout.view_htg_calander, this)
        initCalendar(null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        View.inflate(context, R.layout.view_htg_calander, this)
        initCalendar(attrs)
    }

    private fun initCalendar(attrs: AttributeSet?) {
        ivLeft = findViewById(R.id.iv_htc_left)
        ivRight = findViewById(R.id.iv_htc_right)
        tvTitle = findViewById(R.id.tv_htc_title)
        gvCalendar = findViewById(R.id.gv_htc_calendar)

        if (attrs == null) {
            fontSize = context.resources.getDimension(R.dimen.font_14)
            textColor = Color.BLACK
            btnSize = context.resources.getDimension(R.dimen.dimen_24)
            btnColor = Color.BLUE
            return
        }
        val a = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.HTGCalendarView, 0, 0
        )

        fontSize = a.getDimension(
            R.styleable.HTGCalendarView_fontSize,
            context.resources.getDimension(R.dimen.font_14)
        )
        textColor = a.getColor(R.styleable.HTGCalendarView_textColor, Color.BLACK)
        btnSize = a.getDimension(
            R.styleable.HTGCalendarView_btnSize,
            context.resources.getDimension(R.dimen.dimen_24)
        )
        btnColor = a.getInt(R.styleable.HTGCalendarView_btnColor, Color.BLUE)

        a.recycle()
    }

    private fun setEvent() {
        ivLeft.setOnClickListener {
            selectedDate = selectedDate.diffDate(Calendar.MONTH, -1)
            setCalendar()
        }
        ivRight.setOnClickListener {
            selectedDate = selectedDate.diffDate(Calendar.MONTH, 1)
            setCalendar()
        }
    }

    public override fun onFinishInflate() {
        super.onFinishInflate()

        tvTitle.setTextColor(textColor!!)
        tvTitle.textSize = coverPixelToDP(fontSize!!)

        setCalendar()
        setEvent()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        invalidate()
        Log.e("HTGCalendarView", heightMeasureSpec.toString())
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    @SuppressLint("SimpleDateFormat")
    private fun setCalendar() {
        val sdf = SimpleDateFormat("MMMM yyyy")
        title = sdf.format(selectedDate)
        tvTitle.text = title

        val currentMonth = GregorianCalendar(selectedDate.year, selectedDate.month, 1)
        val daysInMonth = currentMonth.getActualMaximum(Calendar.DAY_OF_MONTH)

        val list = arrayListOf<CalendarItem>()
        list.add(CalendarItem("Sun", true, false, false, false, false, false))
        list.add(CalendarItem("Mon", true, false, false, false, false, false))
        list.add(CalendarItem("Tue", true, false, false, false, false, false))
        list.add(CalendarItem("Wed", true, false, false, false, false, false))
        list.add(CalendarItem("Thu", true, false, false, false, false, false))
        list.add(CalendarItem("Fri", true, false, false, false, false, false))
        list.add(CalendarItem("Sat", true, false, false, false, false, false))

        val moodDate: Date = selectedDate.diffDate(Calendar.DATE, -3)
        val startDate = moodDate.diffDate(Calendar.DATE, -1)
        val endDate = moodDate.diffDate(Calendar.DATE, 15)

        val firstDate = Date(selectedDate.year, selectedDate.month, 1)
        val firstWeek = firstDate.getDay()

        val previousDate = selectedDate.diffDate(Calendar.MONTH, -1)
        val previousMonth = GregorianCalendar(previousDate.year, previousDate.month, 1)
        val daysInPreMonth = previousMonth.getActualMaximum(Calendar.DAY_OF_MONTH)
        for (i: Int in 0 until firstWeek) {
            val currentDate =
                Date(previousDate.year, previousDate.month, (daysInPreMonth - firstWeek + 1 + i))
            val _selected = currentDate.before(endDate) && currentDate.after(startDate)
            val _isFirst = currentDate.toMDY() == startDate.diffDate(Calendar.DATE, 1).toMDY()
            val _isEnd = currentDate.toMDY() == endDate.toMDY()
            val _isAction = currentDate.toMDY() == actionDate.toMDY()
            list.add(
                CalendarItem(
                    (daysInPreMonth - firstWeek + 1 + i).toString(),
                    false,
                    false,
                    _selected,
                    _isFirst,
                    _isEnd,
                    _isAction
                )
            )
        }

        for (i: Int in 1..daysInMonth) {
            val currentDate = Date(selectedDate.year, selectedDate.month, i)
            val _selected = currentDate.before(endDate) && currentDate.after(startDate)
            val _isFirst = currentDate.toMDY() == startDate.diffDate(Calendar.DATE, 1).toMDY()
            val _isEnd = currentDate.toMDY() == endDate.toMDY()
            val _isAction = currentDate.toMDY() == actionDate.toMDY()

            list.add(
                CalendarItem(
                    i.toString(),
                    false,
                    true,
                    _selected,
                    _isFirst,
                    _isEnd,
                    _isAction
                )
            )
        }

        val nextDate = selectedDate.diffDate(Calendar.MONTH, 1)
        val lastDate = Date(selectedDate.year, selectedDate.month, daysInMonth)
        val lastWeek = lastDate.getDay()
        for (i: Int in (lastWeek + 1)..6) {
            val currentDate =
                Date(nextDate.year, nextDate.month, (daysInPreMonth - firstWeek + 1 + i))
            val _selected = currentDate.before(endDate) && currentDate.after(startDate)
            val _isFirst = currentDate.toMDY() == startDate.diffDate(Calendar.DATE, 1).toMDY()
            val _isEnd = currentDate.toMDY() == endDate.toMDY()
            val _isAction = currentDate.toMDY() == actionDate.toMDY()

            list.add(
                CalendarItem(
                    (i - lastWeek).toString(),
                    false,
                    false,
                    _selected,
                    _isFirst,
                    _isEnd,
                    _isAction
                )
            )
        }

        val adapter = GridAdapter(context, R.layout.item_htg_cell, list, object : CalendarListener {
            override fun onClick(item: CalendarItem) {
                actionDate = Date(selectedDate.year, selectedDate.month, item.title.toInt())
                setCalendar()
                if (listener != null) listener!!.onPicker(actionDate)
            }
        })
        gvCalendar.adapter = adapter
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
    }

    private fun coverPixelToDP(dps: Float): Float {
        val scale = this.resources.displayMetrics.density
        return (dps / scale)
    }

    internal class GridAdapter internal constructor(
        context: Context,
        private val resource: Int,
        private val itemList: ArrayList<CalendarItem>,
        private val listener: CalendarListener,
    ) : ArrayAdapter<GridAdapter.ItemHolder>(context, resource) {

        override fun getCount(): Int {
            return this.itemList.size
        }

        @SuppressLint("UseCompatLoadingForDrawables")
        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            var view = convertView

            val holder: ItemHolder
            if (view == null) {
                view = LayoutInflater.from(context).inflate(resource, null)
                holder = ItemHolder()
                holder.llBack = view!!.findViewById(R.id.ll_htg_back)
                holder.clBack = view.findViewById(R.id.cl_htg_back)
                holder.tvDesc = view.findViewById(R.id.tv_htg_cell)
                view.tag = holder
            } else {
                holder = view.tag as ItemHolder
            }

            val item = itemList[position]
            holder.tvDesc!!.text = item.title
            if (item.isWeek) {
                holder.tvDesc!!.textSize = 12.0f
                holder.tvDesc!!.setTextColor(context.getColor(R.color.textColor))
            } else if (item.selected) {
                if (item.isFirst) {
                    holder.llBack!!.background =
                        context.getDrawable(R.drawable.back_calendar_mood_01)
                } else if (item.isEnd) {
                    if (position % 7 == 0) {
                        holder.llBack!!.background =
                            context.getDrawable(R.drawable.back_calendar_mood_04)
                    } else {
                        holder.llBack!!.background =
                            context.getDrawable(R.drawable.back_calendar_mood_03)
                    }
                } else if (!item.current) {
                    holder.llBack!!.background =
                        context.getDrawable(R.drawable.back_calendar_mood_02)
                } else if (position % 7 == 0) {
                    holder.llBack!!.background =
                        context.getDrawable(R.drawable.back_calendar_mood_01)
                } else if (position % 7 == 6) {
                    holder.llBack!!.background =
                        context.getDrawable(R.drawable.back_calendar_mood_03)
                } else {
                    holder.llBack!!.background =
                        context.getDrawable(R.drawable.back_calendar_mood_02)
                }

                if (item.current) {
                    holder.tvDesc!!.setTextColor(context.getColor(R.color.red))
                } else {
                    holder.tvDesc!!.setTextColor(context.getColor(R.color.red_30))
                }
            } else {
                if (item.current) {
                    holder.tvDesc!!.setTextColor(context.getColor(R.color.textColor))
                } else {
                    holder.tvDesc!!.setTextColor(context.getColor(R.color.textColor_30))
                }
            }

            if (item.isAction) {
                holder.clBack!!.background =
                    context.getDrawable(R.drawable.back_calendar_item)
                holder.tvDesc!!.setTextColor(context.getColor(R.color.white))
            }

            if (item.current) {
                holder.clBack!!.setOnClickListener {
                    listener.onClick(item)
                }
            }

            return view
        }

        internal class ItemHolder {
            var tvDesc: TextView? = null
            var llBack: LinearLayout? = null
            var clBack: ConstraintLayout? = null
        }
    }
}