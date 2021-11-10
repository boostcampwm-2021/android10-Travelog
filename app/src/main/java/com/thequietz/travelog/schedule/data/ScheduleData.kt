package com.thequietz.travelog.schedule.data

data class Schedule(
    val date: String,
    val placeList: MutableList<SchedulePlace>,
)

data class SchedulePlace(
    val name: String,
    val color: Int,
)
