package com.thequietz.travelog.schedule.data

data class ScheduleDetailItem(
    val id: Int,
    val type: Int,
    val color: ColorRGB?,
    val name: String?,
    val index: Int?,
)
