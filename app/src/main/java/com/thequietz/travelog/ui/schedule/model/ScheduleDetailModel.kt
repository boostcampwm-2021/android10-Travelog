package com.thequietz.travelog.ui.schedule.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.thequietz.travelog.ui.place.model.PlaceDetailModel
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "ScheduleDetail")
data class ScheduleDetailModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val scheduleId: Int,
    val place: SchedulePlaceModel,
    var date: String,
    val destination: PlaceDetailModel
) : Parcelable
