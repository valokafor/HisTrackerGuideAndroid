package com.hiscycleguide.android.service

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.IntentService
import android.app.Notification
import android.app.PendingIntent
import android.content.Intent
import com.hiscycleguide.android.receiver.AlarmReceiver
import android.os.Build
import android.app.NotificationManager

import androidx.core.app.NotificationCompat

import android.app.NotificationChannel
import android.content.Context
import android.content.IntentFilter
import android.graphics.Color
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.hiscycleguide.android.R


@SuppressLint("UnspecifiedImmutableFlag")
class AlarmService : IntentService("AlarmService") {

    override fun onCreate() {
        super.onCreate()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) startOwnForeground() else startForeground(
            1,
            Notification()
        )
//        startAlarm()
//        LocalBroadcastManager.getInstance(this).registerReceiver(AlarmReceiver(), IntentFilter("alarmReceiver"))
    }

    private fun startOwnForeground() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val NOTIFICATION_CHANNEL_ID = packageName
            val channelName = this.getString(R.string.app_name)
            val chan = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                channelName,
                NotificationManager.IMPORTANCE_NONE
            )
            chan.lightColor = Color.BLUE
            chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
            val manager =
                (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
            manager.createNotificationChannel(chan)
            val notificationBuilder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            val notification: Notification = notificationBuilder.setOngoing(true)
                .setSmallIcon(R.drawable.ic_notification_top)
                .setContentTitle("Alarm service is running in background")
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build()
            startForeground(2, notification)
        }
    }

    override fun onHandleIntent(intent: Intent?) {

    }

}