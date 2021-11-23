package com.thequietz.travelog.record.model

data class Record(
    val travelId: Int,
    val title: String,
    val startDate: String,
    val endDate: String,
    val images: List<String> = emptyList()
)
