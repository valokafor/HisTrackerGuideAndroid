package com.hiscycleguide.android.receiver

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.hiscycleguide.android.R
import com.hiscycleguide.android.activity.MainActivity
import com.hiscycleguide.android.provider.PreferenceProvider
import com.hiscycleguide.android.util.Utils
import com.hiscycleguide.android.util.diffDate
import com.hiscycleguide.android.util.tohma
import java.util.*

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val currentDate = Date()
        val currentTime = currentDate.tohma()
        val alarmTime = PreferenceProvider.getAlarmTime()

        if (currentTime == alarmTime) {
            val notifyDelay = PreferenceProvider.getNotifyTime()
            val compareDate = currentDate.diffDate(Calendar.DATE, (3 - notifyDelay))
            val moodIndex = Utils.getMoodIndex(compareDate)

            if (PreferenceProvider.getOvulationNotify()) {
                if (moodIndex == 14) {
                    showMenstrualNotification(context)
                }
            }

            if (PreferenceProvider.getMenstrualNotify()) {
                if (moodIndex == 0) {
                    showMenstrualNotification(context)
                }
            }

            if (PreferenceProvider.getLutealNotify()) {
                if (moodIndex == 6) {
                    showMenstrualNotification(context)
                }
            }

            if (PreferenceProvider.getFollicularNotify()) {
                if (moodIndex == 20) {
                    showMenstrualNotification(context)
                }
            }
        }
    }

    fun showMenstrualNotification(context: Context?) {
        val NOTIFICATION_CHANNEL_ID = context!!.packageName
        val appName = context.getString(R.string.app_name)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = context.getString(R.string.app_name)
            val descriptionText = "Alarm service is running in background"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, 0)

        val builder = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification_top)
            .setContentTitle(appName)
            .setContentText("Alarm service is running in background")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setCategory(NotificationCompat.CATEGORY_ALARM)

        with(NotificationManagerCompat.from(context)) {
            notify(1001, builder.build())
        }
    }
}