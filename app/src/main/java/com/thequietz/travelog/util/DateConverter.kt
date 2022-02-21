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

/* RecordBasicViewModel.kt */

fun Int.toStringDate(): String {
    if (this / 10 == 0) {
        return "0$this"
    }
    return "$this"
}

fun String.nextDate(): String {
    val tempDate = this.split('.').map { it.toInt() }
    val isLeapYear = tempDate[0] % 4 == 0
    val dayOfMonth =
        if (isLeapYear) listOf(0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)
        else listOf(0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)

    if (tempDate[2] + 1 <= dayOfMonth[tempDate[1]]) {
        return "${tempDate[0]}.${(tempDate[1]).toStringDate()}.${(tempDate[2] + 1).toStringDate()}"
    }

    if (tempDate[1] + 1 <= 12) {
        return "${tempDate[0]}.${(tempDate[1] + 1).toStringDate()}.01"
    }

    return "${tempDate[0] + 1}.01.01"
}

fun createDayFromDate(startDate: String, date: String): String {
    val tempStartDate = startDate.split('.').map { it.toInt() }
    val tempDate = date.split('.').map { it.toInt() }
    val isLeapYear = tempDate[0] % 4 == 0
    val dayOfMonth =
        if (isLeapYear) listOf(0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)
        else listOf(0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)

    // 2020.12.30 ~ 2021.1.4
    var day = 0
    if (tempStartDate[0] < tempDate[0]) {
        day = dayOfMonth[tempStartDate[1]] - tempStartDate[2]

        for (i in tempStartDate[1] + 1..12) {
            day += dayOfMonth[i]
        }

        day += tempDate[2]

        for (i in tempDate[1] - 1 downTo 1) {
            day += dayOfMonth[i]
        }
    }
    // 2021.1.4 ~ 2021.3.5
    else if (tempStartDate[1] < tempDate[1]) {
        day = dayOfMonth[tempStartDate[1]] - tempStartDate[2]

        for (i in tempStartDate[1] + 1 until tempDate[1]) {
            day += dayOfMonth[i]
        }

        day += tempDate[2]
    }
    // 2021.1.4 ~ 2021.1.5
    else {
        day = tempDate[2] - tempStartDate[2]
    }

    return "Day${day + 1}"
}

fun createDateFromDay(startDate: String, day: String): String {
    val tempDate = startDate.split('.').map { it.toInt() }
    val tempDay = day.substring(3).toInt() - 1
    val isLeapYear = tempDate[0] % 4 == 0
    val dayOfMonth =
        if (isLeapYear) listOf(0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)
        else listOf(0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)

    val day = ((tempDate[2] + tempDay) % (dayOfMonth[tempDate[1]] + 1)).toStringDate()
    val month = (tempDate[1] + (tempDate[2] + tempDay) / (dayOfMonth[tempDate[1]]))

    // TODO("2달 이상 고려해야함.")

    if (tempDate[2] + tempDay <= dayOfMonth[tempDate[1]]) {
        return "${tempDate[0]}.${tempDate[1].toStringDate()}.${(tempDate[2] + tempDay).toStringDate()}"
    }

    if (tempDate[1] + 1 <= 12) {
        return "${tempDate[0]}.${(tempDate[1] + 1).toStringDate()}.${((tempDate[2] + tempDay) % dayOfMonth[tempDate[1]]).toStringDate()}"
    }

    return "${tempDate[0] + 1}.01.${((tempDate[2] + tempDay) % dayOfMonth[tempDate[1]]).toStringDate()}"
}
