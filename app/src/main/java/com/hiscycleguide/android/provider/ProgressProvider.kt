package com.hiscycleguide.android.provider

import android.app.ProgressDialog
import android.content.Context
import com.hiscycleguide.android.R

class ProgressProvider(context: Context) : ProgressDialog(context) {

    companion object {
        fun newInstance(context: Context) : ProgressProvider {
            val progressProvider = ProgressProvider(context)
            progressProvider.setTitle(context.getString(R.string.progressTitle))
            progressProvider.setMessage(context.getString(R.string.progressDetail))
            return progressProvider
        }
    }

}