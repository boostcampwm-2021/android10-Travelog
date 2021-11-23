package com.thequietz.travelog.menu.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import com.thequietz.travelog.util.dateToString
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

fun registerAlarm(context: Context, type: AlarmType, alarmTime: String) {
    Log.e("register_alarm", "register $alarmTime")
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val intent = Intent(context, AlarmReceiver::class.java)

    if (type == AlarmType.Schedule) {
        intent.putExtra("content", "오늘의 일정을 확인하세요!")
        intent.putExtra("type", "schedule")
    } else {
        intent.putExtra("content", "오늘의 여행을 기록하세요!")
        intent.putExtra("type", "record")
    }

    val pendingIntent = PendingIntent.getBroadcast(
        context,
        if (type == AlarmType.Schedule) AlarmReceiver.SCHEDULE_NOTIFICATION_ID
        else AlarmReceiver.RECORD_NOTIFICATION_ID,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )

    val calendar = Calendar.getInstance().apply {
        var today = dateToString(Date(System.currentTimeMillis()))
        today = today.replace(".", "-") + " " + alarmTime
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        time = dateFormat.parse(today)
    }

    val now = Date(System.currentTimeMillis())
    if (calendar.time.before(now)) return

    alarmManager.setRepeating(
        AlarmManager.RTC,
        calendar.timeInMillis,
        AlarmManager.INTERVAL_DAY,
        pendingIntent
    )
}

fun cancelAlarm(context: Context, type: AlarmType) {
    Log.e("cancel_alarm", type.name)
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val intent = Intent(context, AlarmReceiver::class.java)

    val pendingIntent = PendingIntent.getBroadcast(
        context,
        if (type == AlarmType.Schedule) AlarmReceiver.SCHEDULE_NOTIFICATION_ID
        else AlarmReceiver.RECORD_NOTIFICATION_ID,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )
    alarmManager.cancel(pendingIntent)
}

enum class AlarmType {
    Schedule, Record
}
