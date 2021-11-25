package com.thequietz.travelog.record.model

data class RecordBasic(
    val travelId: Int,
    val title: String,
    val startDate: String,
    val endDate: String,
    val travelDestinations: List<RecordBasicItem.TravelDestination> = emptyList()
)

sealed class RecordBasicItem {
    data class TravelDestination(
        val name: String,
        val date: String,
        val group: Int,
        val images: List<String> = emptyList(),
        val lat: Double,
        val lng: Double
    ) : RecordBasicItem()

    class RecordBasicHeader(
        val day: String,
        val date: String
    ) : RecordBasicItem()
}
