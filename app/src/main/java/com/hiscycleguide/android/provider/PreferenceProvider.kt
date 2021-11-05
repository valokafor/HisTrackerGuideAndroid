package com.hiscycleguide.android.provider

import android.content.Context
import android.content.SharedPreferences

class PreferenceProvider(context: Context) {

    companion object {
        private lateinit var pref: SharedPreferences

        private const val sharedPrefFile = "com.hiscycleguide.android"
        private const val keyFirstRunning = "is_first_running"

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
    }

    init {
        pref = context.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
    }

}