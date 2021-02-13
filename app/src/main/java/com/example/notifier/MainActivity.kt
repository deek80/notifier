package com.example.notifier

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import java.util.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        createNotificationChannels()

        val btn = findViewById<Button>(R.id.btn)
        btn.setOnClickListener {
            Log.d("CLICK", "button was clicked")
            // showNotification("exact", "Test", "hello, world")
            alarmExampleFromDocs()
        }
    }

    private fun alarmExampleFromDocs() {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmIntent = Intent(this, AlarmReceiver::class.java).let { intent ->
          PendingIntent.getBroadcast(this, 0, intent, 0)
        }

        // Set the alarm to start at 8:30 a.m.
        val calendar: Calendar = Calendar.getInstance().apply {
          timeInMillis = System.currentTimeMillis()
          set(Calendar.HOUR_OF_DAY, 16)
          set(Calendar.MINUTE, 15)
        }

        alarmManager.setRepeating(
          AlarmManager.RTC_WAKEUP,
          calendar.timeInMillis,
          1000 * 60 * 30,
          alarmIntent
        )
        
    }

    private fun showNotification(channel: String, title: String, text: String) {
        // Create an explicit intent for an Activity in your app
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

        val builder = NotificationCompat.Builder(this, channel)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(title)
                .setContentText(text)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)

        with(NotificationManagerCompat.from(this)) {
            // notificationId is a unique int for each notification that you must define
            notify(channel.hashCode(), builder.build())
        }
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val inexact_channel = NotificationChannel("inexact", "using inexact timer", NotificationManager.IMPORTANCE_DEFAULT).apply {
                description = "this is the inexact channel!"
            }

            val exact_channel = NotificationChannel("exact", "using exact timer", NotificationManager.IMPORTANCE_DEFAULT).apply {
                description = "this is the exact channel!"
            }

            // Register the channel with the system
            val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(inexact_channel);
            notificationManager.createNotificationChannel(exact_channel);
        }
    }
}
