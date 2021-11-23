package com.thequietz.travelog.guide

import com.google.gson.annotations.SerializedName

data class RecommendResponse(
    @SerializedName("response") val response: rResponse
)
data class rResponse(
    @SerializedName("body") val body: rBody
)
data class rBody(
    @SerializedName("items") val items: rItems,
    @SerializedName("totalCount") val totalCnt: Int
)
data class rItems(
    @SerializedName("item") val item: List<RecommendPlace>
)
data class RecommendPlace(
    @SerializedName("title") val name: String = "",
    @SerializedName("firstimage") val url: String = "",
    @SerializedName("addr1") val description: String = "",
    @SerializedName("readcount") val readCount: String,
    @SerializedName("mapx") val longitude: Double = 0.0,
    @SerializedName("mapy") val latitude: Double = 0.0
)
