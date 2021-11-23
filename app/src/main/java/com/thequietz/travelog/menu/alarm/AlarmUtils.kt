package com.thequietz.travelog.menu.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import java.text.SimpleDateFormat
import java.util.Calendar


fun registerAlarm(context: Context, type: AlarmType, date: String, id: Int, alarmTime: String) {
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val intent = Intent(context, AlarmReceiver::class.java)

    if(type == AlarmType.Schedule) {
        intent.putExtra("content", "오늘의 일정을 확인하세요!")
        intent.putExtra("type", "schedule")
    }
    else{
        intent.putExtra("content", "오늘의 여행을 기록하세요!")
        intent.putExtra("type", "record")
    }
    intent.putExtra("id", id)

    val pendingIntent = PendingIntent.getBroadcast(
        context,
        id,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )

    val calendar = Calendar.getInstance().apply {
      //  val dateStr = "2021-$date $alarmTime"
        val dateStr = "2021-11-23 08:17:00"
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        time = dateFormat.parse(dateStr)
    }

    alarmManager.setRepeating(
        AlarmManager.RTC,
        calendar.timeInMillis,
        1000,
        pendingIntent
    )

}

fun cancelAlarm(context: Context, id: Int) {
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val intent = Intent(context, AlarmReceiver::class.java)

    val pendingIntent = PendingIntent.getBroadcast(
        context,
        id,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )
    alarmManager.cancel(pendingIntent)
}

enum class AlarmType {
    Schedule, Record
}
