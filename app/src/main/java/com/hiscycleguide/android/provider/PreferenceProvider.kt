package com.hiscycleguide.android.provider

import android.content.Context
import android.content.SharedPreferences
import com.hiscycleguide.android.model.UserModel

class PreferenceProvider(context: Context) {

    companion object {
        private lateinit var pref: SharedPreferences

        private const val sharedPrefFile = "com.hiscycleguide.android"
        private const val keyFirstRunning = "is_first_running"

        private const val keyOvulationPhase = "key_ovulation_phase"
        private const val keyMenstrualPhase = "key_menstrual_phase"
        private const val keyLutealPhase = "key_luteal_phase"
        private const val keyFollicularPhase = "key_follicular_phase"
        private const val keyNotifyTime = "key_notify_time"
        private const val keyAlarmTime = "key_alarm_time"
        private const val keyCurrentUser = "key_current_user"

        fun newInstance(context: Context) : PreferenceProvider {
            return PreferenceProvider(context)
        }

        fun getFirstRunning() : Boolean {
            return pref.getBoolean(keyFirstRunning, true)
        }

        fun setFirstRunning() {
            val editor = pref.edit()
            editor.putBoolean(keyFirstRunning, false)
            editor.apply()
        }

        fun getOvulationNotify() : Boolean {
            return pref.getBoolean(keyOvulationPhase, false)
        }

        fun setOvulationNotify(boolean: Boolean) {
            val editor = pref.edit()
            editor.putBoolean(keyOvulationPhase, boolean)
            editor.apply()
        }

        fun getMenstrualNotify() : Boolean {
            return pref.getBoolean(keyMenstrualPhase, false)
        }

        fun setMenstrualNotify(boolean: Boolean) {
            val editor = pref.edit()
            editor.putBoolean(keyMenstrualPhase, boolean)
            editor.apply()
        }

        fun getLutealNotify() : Boolean {
            return pref.getBoolean(keyLutealPhase, false)
        }

        fun setLutealNotify(boolean: Boolean) {
            val editor = pref.edit()
            editor.putBoolean(keyLutealPhase, boolean)
            editor.apply()
        }

        fun getFollicularNotify() : Boolean {
            return pref.getBoolean(keyFollicularPhase, false)
        }

        fun setFollicularNotify(boolean: Boolean) {
            val editor = pref.edit()
            editor.putBoolean(keyFollicularPhase, boolean)
            editor.apply()
        }

        fun getNotifyTime() : Int {
            return pref.getInt(keyNotifyTime, 0)
        }

        fun setNotifyTime(option: Int) {
            val editor = pref.edit()
            editor.putInt(keyNotifyTime, option)
            editor.apply()
        }

        fun getAlarmTime() : String? {
            return pref.getString(keyAlarmTime, "00:00 am")
        }

        fun setAlarmTime(time: String) {
            val editor = pref.edit()
            editor.putString(keyAlarmTime, time)
            editor.apply()
        }

        fun getCurrentUser() : String? {
            return pref.getString(keyCurrentUser, null)
        }

        fun setCurrentUser(user: UserModel) {
            val editor = pref.edit()
            editor.putString(keyCurrentUser, user.toJson().toString())
            editor.apply()
        }
    }

    init {
        pref = context.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
    }

}