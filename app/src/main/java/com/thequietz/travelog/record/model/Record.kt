package com.thequietz.travelog.record.model

data class Record(
    val title: String,
    val startDate: String,
    val endDate: String,
    val images: List<String> = emptyList()
)
