package com.hiscycleguide.android

import android.app.Application
import com.hiscycleguide.android.provider.FirebaseProvider
import com.hiscycleguide.android.provider.PreferenceProvider

class HTGApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        PreferenceProvider.newInstance(this)
        FirebaseProvider.newInstance()
    }

}