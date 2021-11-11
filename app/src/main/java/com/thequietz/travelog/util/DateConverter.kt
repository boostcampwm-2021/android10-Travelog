package com.thequietz.travelog.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

val dateFormat: SimpleDateFormat = SimpleDateFormat("MM.dd", Locale("ko", "KR"))

fun dateToString(date: Date): String {
    return dateFormat.format(date)
}

fun stringToDate(str: String): Date? {
    return dateFormat.parse(str)
}