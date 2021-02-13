package com.example.notifier

import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import java.util.*

fun todayAt(hour:Int, minute:Int):Calendar {
    return Calendar.getInstance().apply {
        timeInMillis = System.currentTimeMillis()
        set(Calendar.HOUR_OF_DAY, hour)
        set(Calendar.MINUTE, minute)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }
}

fun showNotification(context: Context, reminderId: Int) {
    val notification = NotificationCompat.Builder(context, "reminders")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("the title")
            .setContentText("the main text of the notification")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            //.setContentIntent(openMainActivity)
            .setAutoCancel(true)
            .build()

    with(NotificationManagerCompat.from(context)) {
        notify(reminderId, notification)
    }
}

//fun fetchReminder(reminderId: Int): Reminder {
//    // TODO: actually fetch from db
//    return Reminder()
//}

fun storeReminder(reminderId: Int) {
    // TODO: implement
}