package com.hiscycleguide.android.calendar

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.GridView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import com.hiscycleguide.android.R
import com.hiscycleguide.android.model.UserModel
import com.hiscycleguide.android.util.*
import java.text.SimpleDateFormat
import java.util.*


class HTGCalendarView : LinearLayout {

//    private lateinit var ivLeft: ImageView
//    private lateinit var ivRight: ImageView
    private lateinit var cvLeft: CardView
    private lateinit var cvRight: CardView
    private lateinit var tvTitle: TextView
    private lateinit var gvCalendar: GridView

    private var title: String? = null
    private var textColor: Int? = Color.BLACK
    private var btnSize: Float? = 24.0f
    private var btnColor: Int? = Color.BLUE

    private var actionDate: Date = Date()
    private var selectedDate: Date = Date()

    private var period = 28

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
        cvLeft = findViewById(R.id.cv_calendar_back)
        cvRight = findViewById(R.id.cv_calendar_next)

        tvTitle = findViewById(R.id.tv_htc_title)
        gvCalendar = findViewById(R.id.gv_htc_calendar)

        period = UserModel.getCurrentUser().period.toInt()

        if (attrs == null) {
            textColor = Color.BLACK
            btnSize = context.resources.getDimension(R.dimen.dimen_24)
            btnColor = Color.BLUE
            return
        }
        val a = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.HTGCalendarView, 0, 0
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

        tvTitle.setTextColor(textColor!!)

        setCalendar()
        setEvent()
    }

    @SuppressLint("SimpleDateFormat")
    private fun setCalendar() {
        val sdf = SimpleDateFormat("MMMM yyyy")
        title = sdf.format(selectedDate)
        tvTitle.text = title

        val list = arrayListOf<CalendarItem>()
        list.add(CalendarItem("Sun", true, false, MoodType.Normal, false, false, false))
        list.add(CalendarItem("Mon", true, false, MoodType.Normal, false, false, false))
        list.add(CalendarItem("Tue", true, false, MoodType.Normal, false, false, false))
        list.add(CalendarItem("Wed", true, false, MoodType.Normal, false, false, false))
        list.add(CalendarItem("Thu", true, false, MoodType.Normal, false, false, false))
        list.add(CalendarItem("Fri", true, false, MoodType.Normal, false, false, false))
        list.add(CalendarItem("Sat", true, false, MoodType.Normal, false, false, false))

        val firstDate = Date(selectedDate.year, selectedDate.month, 1)
        val firstWeek = firstDate.getDay()

        val previousDate = selectedDate.diffDate(Calendar.MONTH, -1)
        val previousMonth = GregorianCalendar(previousDate.year, previousDate.month, 1)
        val daysInPreMonth = previousMonth.getActualMaximum(Calendar.DAY_OF_MONTH)
        for (i: Int in 0 until firstWeek) {
            val currentDate =
                Date(previousDate.year, previousDate.month, (daysInPreMonth - firstWeek + 1 + i))

            list.add(
                CalendarItem(
                    currentDate.tod(),
                    false,
                    false,
                    Utils.getMoodType(currentDate),
                    Utils.isFirstDay(currentDate),
                    Utils.isEndDay(currentDate),
                    false,
                )
            )
        }

        val currentMonth = GregorianCalendar(selectedDate.year, selectedDate.month, 1)
        val daysInMonth = currentMonth.getActualMaximum(Calendar.DAY_OF_MONTH)
        for (i: Int in 1..daysInMonth) {
            val currentDate = Date(selectedDate.year, selectedDate.month, i)

            list.add(
                CalendarItem(
                    currentDate.tod(),
                    false,
                    true,
                    Utils.getMoodType(currentDate),
                    Utils.isFirstDay(currentDate),
                    Utils.isEndDay(currentDate),
                    currentDate.toMDY() == actionDate.toMDY(),
                )
            )
        }

        val nextDate = selectedDate.diffDate(Calendar.MONTH, 1)
        val lastDate = Date(selectedDate.year, selectedDate.month, daysInMonth)
        val lastWeek = lastDate.getDay()
        for (i: Int in (lastWeek + 1)..6) {
            val currentDate =
                Date(nextDate.year, nextDate.month, (i - lastWeek))

            list.add(
                CalendarItem(
                    currentDate.tod(),
                    false,
                    false,
                    Utils.getMoodType(currentDate),
                    Utils.isFirstDay(currentDate),
                    Utils.isEndDay(currentDate),
                    false,
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
            } else if (item.type == MoodType.Menstruation) {
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
            } else if (item.type == MoodType.Ovulation) {
                if (item.isFirst) {
                    holder.llBack!!.background =
                        context.getDrawable(R.drawable.back_calendar_ovul_01)
                } else if (item.isEnd) {
                    if (position % 7 == 0) {
                        holder.llBack!!.background =
                            context.getDrawable(R.drawable.back_calendar_ovul_04)
                    } else {
                        holder.llBack!!.background =
                            context.getDrawable(R.drawable.back_calendar_ovul_03)
                    }
                } else if (!item.current) {
                    holder.llBack!!.background =
                        context.getDrawable(R.drawable.back_calendar_ovul_02)
                } else if (position % 7 == 0) {
                    holder.llBack!!.background =
                        context.getDrawable(R.drawable.back_calendar_ovul_01)
                } else if (position % 7 == 6) {
                    holder.llBack!!.background =
                        context.getDrawable(R.drawable.back_calendar_ovul_03)
                } else {
                    holder.llBack!!.background =
                        context.getDrawable(R.drawable.back_calendar_ovul_02)
                }

                if (item.current) {
                    holder.tvDesc!!.setTextColor(context.getColor(R.color.yellow))
                } else {
                    holder.tvDesc!!.setTextColor(context.getColor(R.color.yellow_30))
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