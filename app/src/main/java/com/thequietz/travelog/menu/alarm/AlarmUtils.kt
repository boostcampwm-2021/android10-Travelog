package com.thequietz.travelog.menu.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

fun registerAlarm(context: Context, type: AlarmType, date: Date?) {
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val intent = Intent(context, AlarmReceiver::class.java)
    intent.putExtra("type", type)

    val pendingIntent = PendingIntent.getBroadcast(
        context,
        AlarmReceiver.NOTIFICATION_ID,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )

    val calendar = Calendar.getInstance().apply {
        val dateStr = "2021-11-17 17:00:00"
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        time = dateFormat.parse(dateStr)
        // time = date
    }

    alarmManager.set(
        AlarmManager.RTC,
        calendar.timeInMillis,
        pendingIntent
    )
}

fun cancelAlarm() {
}

enum class AlarmType {
    Schedule, Record
}
