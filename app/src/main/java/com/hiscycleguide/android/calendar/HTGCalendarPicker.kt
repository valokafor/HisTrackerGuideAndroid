package com.hiscycleguide.android.calendar

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import com.hiscycleguide.android.R
import com.hiscycleguide.android.util.diffDate
import com.hiscycleguide.android.util.toMDY
import java.text.SimpleDateFormat
import java.util.*


class HTGCalendarPicker : LinearLayout {

//    private lateinit var ivLeft: ImageView
//    private lateinit var ivRight: ImageView
    private lateinit var cvLeft: CardView
    private lateinit var cvRight: CardView
    private lateinit var tvTitle: TextView
    private lateinit var gvCalendar: GridView
    private lateinit var llHeader: LinearLayout

    private var title: String? = null
    private var headerVisible: Boolean = true

    private var selectedDate: Date = Date()
    private var actionDate: Date = Date()

    private var listener: OnCalendarListener? = null

    fun setOnCalendarListener(listener: OnCalendarListener) {
        this.listener = listener
    }

    constructor(context: Context) : super(context) {
        View.inflate(context, R.layout.picker_htg_calander, this)
        initCalendar(null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        View.inflate(context, R.layout.picker_htg_calander, this)
        initCalendar(attrs)
    }

    private fun initCalendar(attrs: AttributeSet?) {
//        ivLeft = findViewById(R.id.iv_htc_left)
//        ivRight = findViewById(R.id.iv_htc_right)
        cvLeft = findViewById(R.id.cv_calendar_back)
        cvRight = findViewById(R.id.cv_calendar_next)
        tvTitle = findViewById(R.id.tv_htc_title)
        gvCalendar = findViewById(R.id.gv_htc_calendar)
        llHeader = findViewById(R.id.ll_calendar_header)

        if (attrs == null) {
            headerVisible = true
            return
        }
        val a = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.HTGCalendarPicker, 0, 0
        )
        headerVisible = a.getBoolean(R.styleable.HTGCalendarPicker_headerVisible, true)
        a.recycle()
    }

    private fun setEvent() {
        cvLeft.setOnClickListener {
            selectedDate = selectedDate.diffDate(Calendar.MONTH, -1)
            setCalendar()
        }
        cvRight.setOnClickListener {
            selectedDate = selectedDate.diffDate(Calendar.MONTH, 1)
            setCalendar()
        }
    }

    public override fun onFinishInflate() {
        super.onFinishInflate()

        setCalendar()
        setEvent()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        invalidate()
        Log.e("HTGCalendarView", heightMeasureSpec.toString())
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    fun setActionDate(date: Date) {
        actionDate = date
        setCalendar()
    }

    @SuppressLint("SimpleDateFormat")
    private fun setCalendar() {
        if (headerVisible) {
            llHeader.visibility = VISIBLE
        } else {
            llHeader.visibility = GONE
        }

        val sdf = SimpleDateFormat("MMMM yyyy")
        title = sdf.format(selectedDate)
        tvTitle.text = title

        val currentMonth = GregorianCalendar(selectedDate.year, selectedDate.month, 1)
        val daysInMonth = currentMonth.getActualMaximum(Calendar.DAY_OF_MONTH)

        val list = arrayListOf<CalendarPickerItem>()
        list.add(CalendarPickerItem("Sun", true, false, false))
        list.add(CalendarPickerItem("Mon", true, false, false))
        list.add(CalendarPickerItem("Tue", true, false, false))
        list.add(CalendarPickerItem("Wed", true, false, false))
        list.add(CalendarPickerItem("Thu", true, false, false))
        list.add(CalendarPickerItem("Fri", true, false, false))
        list.add(CalendarPickerItem("Sat", true, false, false))

        val firstDate = Date(selectedDate.year, selectedDate.month, 1)
        val firstWeek = firstDate.getDay()

        val previousDate = selectedDate.diffDate(Calendar.MONTH, -1)
        val previousMonth = GregorianCalendar(previousDate.year, previousDate.month, 1)
        val daysInPreMonth = previousMonth.getActualMaximum(Calendar.DAY_OF_MONTH)
        for (i: Int in 0 until firstWeek) {
            list.add(
                CalendarPickerItem(
                    (daysInPreMonth - firstWeek + 1 + i).toString(),
                    false,
                    false,
                    false,
                )
            )
        }

        for (i: Int in 1..daysInMonth) {
            val currentDate = Date(selectedDate.year, selectedDate.month, i)
            val _isAction = currentDate.toMDY() == actionDate.toMDY()

            list.add(
                CalendarPickerItem(
                    i.toString(),
                    false,
                    true,
                    _isAction,
                )
            )
        }

        val lastDate = Date(selectedDate.year, selectedDate.month, daysInMonth)
        val lastWeek = lastDate.getDay()
        for (i: Int in (lastWeek + 1)..6) {
            list.add(
                CalendarPickerItem(
                    (i - lastWeek).toString(),
                    false,
                    false,
                    false,
                )
            )
        }

        val adapter =
            GridAdapter(context, R.layout.item_htg_cell, list, object : CalendarPickerListener {
                override fun onClick(item: CalendarPickerItem) {
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
        private val itemList: ArrayList<CalendarPickerItem>,
        private val listener: CalendarPickerListener,
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