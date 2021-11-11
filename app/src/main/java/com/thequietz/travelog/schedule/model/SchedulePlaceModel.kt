package com.thequietz.travelog.schedule.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class PlaceModel(
    @SerializedName("firstimage")
    var thumbnail: String,

    @SerializedName("areacode")
    val areaCode: Int,

    @SerializedName("mapx")
    val mapX: Float,

    @SerializedName("mapy")
    val mapY: Float,

    @SerializedName("cityname")
    val cityName: String,
) : Parcelable {
    override fun toString(): String {
        return cityName
    }
}

data class PlaceResponse(
    @SerializedName("data")
    val data: List<PlaceModel>,
)

data class PlaceSelected(
    val index: Int,
    val value: String,
)
