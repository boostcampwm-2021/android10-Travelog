package com.thequietz.travelog.guide

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class AllPlaceResponse(
    @SerializedName("data") val data: List<Place>
)
data class Place(
    @SerializedName("firstimage") val url: String,
    @SerializedName("cityname") val name: String,
    @SerializedName("areacode") val areaCode: String,
    @SerializedName("sigungucode") val sigunguCode: String
) : Serializable
