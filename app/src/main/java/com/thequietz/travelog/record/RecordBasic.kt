package com.thequietz.travelog.record

data class RecordBasic(
    val title: String,
    val startDate: String,
    val endDate: String,
    val travelDestinations: List<RecordBasicItem.TravelDestination> = emptyList()
)

sealed class RecordBasicItem {
    data class TravelDestination(
        val name: String,
        val date: String,
        val images: List<String> = emptyList()
    ) : RecordBasicItem()

    class RecordBasicHeader(
        val day: String
    ) : RecordBasicItem()
}
