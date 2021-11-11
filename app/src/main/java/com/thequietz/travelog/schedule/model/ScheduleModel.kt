package com.thequietz.travelog.schedule.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "Schedule")
data class ScheduleModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String = "",
    val place: List<PlaceModel> = listOf(),
    val date: String = ""
) : Parcelable {
    override fun toString(): String {
        val placeString = place.fold("") { sum, element -> sum + element }
        return placeString.substring(1, placeString.length - 1)
    }
}
