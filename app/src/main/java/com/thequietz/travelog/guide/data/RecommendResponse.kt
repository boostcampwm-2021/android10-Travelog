package com.thequietz.travelog.guide

import com.google.gson.annotations.SerializedName

data class RecommendResponse(
    @SerializedName("response") val response: rResponse
)
data class rResponse(
    @SerializedName("body") val body: rBody
)
data class rBody(
    @SerializedName("items") val items: rItems
)
data class rItems(
    @SerializedName("item") val item: List<RecommendPlace>
)
data class RecommendPlace(
    @SerializedName("title") val name: String = "",
    @SerializedName("firstimage") val url: String = "",
    @SerializedName("addr1") val description: String = ""
)
