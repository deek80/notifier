package com.example.notifier

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        context?.let {
            showNotification(it, "inexact", "Inexact!", "Should be on the 15-minute intervals")
        }
    }

    private fun showNotification(context: Context, channel: String, title: String, text: String) {
        // Create an explicit intent for an Activity in your app
        val openMainActivity = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }.let {
            PendingIntent.getActivity(context, 0, it, 0)
        }

        val notification = NotificationCompat.Builder(context, channel)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(title)
                .setContentText(text)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(openMainActivity)
                .setAutoCancel(true)
                .build()

        with(NotificationManagerCompat.from(context)) {
            // notificationId is a unique int for each notification that you must define
            // TODO: use alarm id since channel will be shared. for now: id=channel.hashCode()
            notify(channel.hashCode(), notification)
        }
    }

}
