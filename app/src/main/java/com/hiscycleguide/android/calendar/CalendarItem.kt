package com.hiscycleguide.android.calendar

import com.hiscycleguide.android.util.MoodType

class CalendarItem (
    var title: String,
    var isWeek: Boolean,
    var current: Boolean,
    var type: MoodType,
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