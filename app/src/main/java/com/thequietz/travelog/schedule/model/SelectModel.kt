package com.thequietz.travelog.schedule.model

import com.google.gson.annotations.SerializedName

data class SelectModel(
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

data class SelectResponse(
    @SerializedName("data")
    val data: List<SelectModel>,
)

data class SelectedData(
    val index: Int,
    val value: String,
)
