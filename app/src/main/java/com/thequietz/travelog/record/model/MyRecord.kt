package com.thequietz.travelog.record.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.thequietz.travelog.data.db.dao.JoinRecord

sealed class MyRecord {
    data class RecordSchedule(val day: String = "") : MyRecord()
    data class RecordPlace(
        val place: String = ""
    ) : MyRecord()

    data class RecordImageList(
        var list: MutableList<JoinRecord> = mutableListOf()
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
    val lat: Double = 0.0,
    val lng: Double = 0.0
)
data class PlaceAndSchedule(
    val place: String = "",
    val day: String = ""
)
