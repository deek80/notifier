package com.example.notifier

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("DON", "alarm received")
        if (context == null || intent == null) {
            return
        }

        showNotification(context, "reminders", "Your Notification", "Hello, World!")
    }

    private fun showNotification(context: Context, channel: String, title: String, text: String) {
        // Create an explicit intent for an Activity in your app
        val openMainActivity = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }.let {
            PendingIntent.getActivity(context, 0, it, 0)
        }

    }

}
