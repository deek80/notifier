package com.example.notifier

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        createRemindersChannel()

        findViewById<Button>(R.id.btn).setOnClickListener {
            Log.d("DON", "button was clicked")
            createExampleAlarm()
        }
    }

    private fun createExampleAlarm() {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmIntent = Intent(this, AlarmReceiver::class.java).let {
            PendingIntent.getBroadcast(this, 1234, it, 0)
        }

        alarmManager.cancel(alarmIntent)
        alarmManager.setRepeating(
          AlarmManager.RTC_WAKEUP,
          todayAt(10, 45).timeInMillis,
          AlarmManager.INTERVAL_HALF_DAY,
          alarmIntent
        )
        
    }

    private fun createRemindersChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val remindersChannel = NotificationChannel("reminders", "Reminders", NotificationManager.IMPORTANCE_DEFAULT).apply {
                description = "Channel for the user's reminders"
            }

            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(remindersChannel);
        }
    }
}
