package com.thequietz.travelog.schedule.data

data class Schedule(
    val date: String,
    val placeList: MutableList<SchedulePlace>,
)

data class SchedulePlace(
    val name: String,
    val color: ColorRGB,
)

data class ColorRGB(
    val r: Int,
    val g: Int,
    val b: Int,
    var id: Int = 0
)
