package com.hiscycleguide.android.calendar

import java.util.*

interface CalendarListener {
    fun onClick(item: CalendarItem)
}

interface CalendarPickerListener {
    fun onClick(item: CalendarPickerItem)
}

interface OnCalendarListener {
    fun onPicker(date: Date)
}