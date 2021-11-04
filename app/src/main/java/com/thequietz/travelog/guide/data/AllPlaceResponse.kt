package com.thequietz.travelog.guide

import com.google.gson.annotations.SerializedName

data class AllPlaceResponse(
    @SerializedName("data") val data: List<Place>
)
data class Place(
    @SerializedName("firstimage") val url: String,
    @SerializedName("cityname") val name: String
)

data class RecommendPlace(
    @SerializedName("title") val name: String = "",
    @SerializedName("firstimage") val url: String = "",
    @SerializedName("addr1") val description: String = ""
)
