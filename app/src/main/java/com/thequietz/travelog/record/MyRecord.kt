package com.thequietz.travelog.record

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
data class RecordImage(
    val schedule: String = "",
    val place: String = "",
    val img: String = "",
    var comment: String = "",
    var group: Int = 0
)
