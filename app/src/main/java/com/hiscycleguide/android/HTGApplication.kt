package com.hiscycleguide.android

import android.app.Application
import android.content.Intent
import com.hiscycleguide.android.provider.FirebaseProvider
import com.hiscycleguide.android.provider.PreferenceProvider
import com.hiscycleguide.android.service.AlarmService

class HTGApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        PreferenceProvider.newInstance(this)
        FirebaseProvider.newInstance()

//        val alarmService = Intent(this, AlarmService::class.java)
//        startService(alarmService)
    }

}