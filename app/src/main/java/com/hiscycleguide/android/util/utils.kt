package com.hiscycleguide.android.util

import com.hiscycleguide.android.model.UserModel
import java.util.*
import kotlin.math.roundToInt

class Utils {

    companion object {

        private var defaultDuring = 6

        const val BROADCAST_TIMER = "com.hiscycleguide.android.BROADCAST_TIMER"

        fun getMoodType(date: Date) : MoodType {
            val startDate = UserModel.getCurrentUser().mood.toDateYMD()!!
            val moodPeriod: Int = UserModel.getCurrentUser().period.toInt()

            val diffDay = date.diffDay(startDate)
            val edd : Int = diffDay % moodPeriod
            if (edd < defaultDuring) {
                return MoodType.Menstruation
            }
            if (edd > moodPeriod / 2 - 1 && edd < moodPeriod / 2 + defaultDuring) {
                return MoodType.Ovulation
            }
            return MoodType.Normal
        }

        fun getMoodIndex(date: Date) : Int {
            val startDate = UserModel.getCurrentUser().mood.toDateYMD()!!
            val moodPeriod: Int = UserModel.getCurrentUser().period.toInt()

            val diffDay = date.diffDay(startDate)
            return diffDay % moodPeriod
        }

        fun isFirstDay(date: Date) : Boolean {
            val moodDate = UserModel.getCurrentUser().mood.toDateYMD()!!
            val moodPeriod: Int = UserModel.getCurrentUser().period.toInt()
            val diffDay = date.diffDay(moodDate)
            return diffDay % moodPeriod == 0 || diffDay % moodPeriod == moodPeriod / 2
        }

        fun isEndDay(date: Date) : Boolean {
            val moodDate = UserModel.getCurrentUser().mood.toDateYMD()!!
            val moodPeriod: Int = UserModel.getCurrentUser().period.toInt()
            val diffDay = date.diffDay(moodDate)
            return diffDay % moodPeriod == defaultDuring - 1  || diffDay % moodPeriod == moodPeriod / 2 + defaultDuring - 1
        }
    }

}