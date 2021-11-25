package com.thequietz.travelog.record.model

import androidx.room.Entity
import androidx.room.PrimaryKey

sealed class MyRecord {
    data class RecordSchedule(val date: String = "") : MyRecord()
    data class RecordPlace(
        val place: String = ""
    ) : MyRecord()

    data class RecordImageList(
        var list: MutableList<RecordImage> = mutableListOf()
    ) : MyRecord()
}

enum class ViewType {
    DATE, PLACE, IMAGE
}

@Entity(tableName = "RecordImage")
data class RecordImage(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val travelId: Int = 1,
    val title: String = "",
    val startDate: String = "",
    val endDate: String = "",
    val day: String = "",
    val place: String = "",
    val url: String = "",
    var group: Int = 0,
    var comment: String = "",
    val lat: Double = 0.0,
    val lng: Double = 0.0
)
