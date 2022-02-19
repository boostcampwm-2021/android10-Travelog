package com.thequietz.travelog.ui.schedule.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class SchedulePlaceModel(
    @SerializedName("firstimage")
    var thumbnail: String,

    @SerializedName("areacode")
    val areaCode: Int,

    @SerializedName("mapx")
    val mapX: Double,

    @SerializedName("mapy")
    val mapY: Double,

    @SerializedName("cityname")
    val cityName: String,

    var isSelected: Boolean = false,
) : Parcelable {
    override fun toString(): String {
        return cityName
    }
}

data class PlaceResponse(
    @SerializedName("data")
    val data: List<SchedulePlaceModel>,
)
