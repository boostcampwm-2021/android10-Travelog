package com.thequietz.travelog.record

data class RecordBasic(
    val title: String,
    val startDate: String,
    val endDate: String,
    val travelDestinations: List<TravelDestination> = emptyList()
)

data class TravelDestination(
    val name: String,
    val date: String,
    val images: List<String> = emptyList()
)
