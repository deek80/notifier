package com.example.notifier

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("ALARM", "received alarm")
        Toast.makeText(context, "received alarm notification", Toast.LENGTH_SHORT).show()
    }
}
