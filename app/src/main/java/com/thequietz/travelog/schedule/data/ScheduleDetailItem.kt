package com.thequietz.travelog.schedule.data

data class ScheduleDetailItem(
    val id: Int,
    val type: Int,
    val color: ColorRGB = ColorRGB(0, 0, 0),
    val name: String?,
    val index: Int?,
)
