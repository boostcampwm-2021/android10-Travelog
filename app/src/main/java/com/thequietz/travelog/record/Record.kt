package com.thequietz.travelog.record

data class Record(
    val title: String,
    val startDate: String,
    val endDate: String,
    val images: List<String>? = null
)
