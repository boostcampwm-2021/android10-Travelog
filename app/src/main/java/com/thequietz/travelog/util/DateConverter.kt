package com.thequietz.travelog.util

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

val dateFormat: SimpleDateFormat = SimpleDateFormat("yyyy.MM.dd", Locale("ko", "KR"))

fun dateToString(date: Date): String {
    return dateFormat.format(date)
}

fun stringToDate(str: String): Date? {
    return dateFormat.parse(str)
}

fun addOneDate(date: String): String {
    val cal: Calendar = Calendar.getInstance()
    cal.apply {
        time = stringToDate(date)
        add(Calendar.DATE, 1)
    }
    return stringToDate(dateToString(cal.time))?.let { dateToString(it) } ?: "2000.01.01"
}
