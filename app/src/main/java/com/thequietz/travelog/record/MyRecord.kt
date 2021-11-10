package com.thequietz.travelog.record

import androidx.room.Entity
import androidx.room.PrimaryKey

sealed class MyRecord {
    data class RecordSchedule(val date: Int = 0) : MyRecord()
    data class RecordPlace(val place: List<String> = listOf()) : MyRecord()
    data class RecordImageList(
        var list: MutableList<RecordImage> = mutableListOf()
    ) : MyRecord()
}
enum class ViewType {
    DATE, PLACE, IMAGE
}
@Entity(tableName = "RecordImage")
data class RecordImage(
    val schedule: String = "",
    val place: String = "",
    val img: String = "",
    var comment: String = "",
    var group: Int = 0,
    @PrimaryKey val id: Int = 0
)
