package com.hiscycleguide.android.calendar

class CalendarItem (
    var title: String,
    var isWeek: Boolean,
    var current: Boolean,
    var selected: Boolean,
    var isFirst: Boolean,
    var isEnd: Boolean,
    var isAction: Boolean,
)

class CalendarPickerItem(
    var title: String,
    var isWeek: Boolean,
    var current: Boolean,
    var isAction: Boolean,
)