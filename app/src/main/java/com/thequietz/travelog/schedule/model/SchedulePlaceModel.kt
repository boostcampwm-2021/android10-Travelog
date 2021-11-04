package com.thequietz.travelog.schedule.model

import com.google.gson.annotations.SerializedName

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
)

data class PlaceResponse(
    @SerializedName("data")
    val data: List<PlaceModel>,
)

data class PlaceSelected(
    val index: Int,
    val value: String,
)
