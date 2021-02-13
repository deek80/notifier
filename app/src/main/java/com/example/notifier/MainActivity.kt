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
        val alarmUniqueId = 1234;
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmIntent = Intent(this, AlarmReceiver::class.java).let {
          PendingIntent.getBroadcast(this, alarmUniqueId, it, 0)
        }

        // Set the alarm to start at 8:30 a.m.
        val calendar: Calendar = Calendar.getInstance().apply {
          timeInMillis = System.currentTimeMillis()
          set(Calendar.HOUR_OF_DAY, 16)
          set(Calendar.MINUTE, 45)
          set(Calendar.SECOND, 0)
          set(Calendar.MILLISECOND, 0)
        }

        alarmManager.cancel(alarmIntent)
        alarmManager.setRepeating(
          AlarmManager.RTC_WAKEUP,
          calendar.timeInMillis,
          1000 * 60 * 15,
          alarmIntent
        )
        
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
