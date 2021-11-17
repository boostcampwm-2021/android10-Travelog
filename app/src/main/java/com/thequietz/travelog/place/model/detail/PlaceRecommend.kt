package com.thequietz.travelog.place.model.detail

import com.google.gson.annotations.SerializedName

data class RecommendResponse(
    @SerializedName("response")
    val response: RecommendBody
)

data class RecommendBody(
    @SerializedName("body")
    val body: RecommendItems
)

data class RecommendItems(
    @SerializedName("items")
    val items: RecommendItem
)

data class RecommendItem(
    @SerializedName("item")
    val info: RecommendInfo
)

data class RecommendInfo(
    @SerializedName("addr1")
    val address: String,

    @SerializedName("mapy")
    val latitude: Double,

    @SerializedName("mapx")
    val longitude: Double,

    @SerializedName("tel")
    val phoneNumber: String = "",

    @SerializedName("title")
    val name: String,

    @SerializedName("overview")
    val overview: String = "",

    @SerializedName("homepage")
    val url: String,
)

data class RecommendImageResponse(
    @SerializedName("response")
    val response: RecommendImageBody
)

data class RecommendImageBody(
    @SerializedName("body")
    val body: RecommendImageItems
)

data class RecommendImageItems(
    @SerializedName("items")
    val items: RecommendImageItem
)

data class RecommendImageItem(
    @SerializedName("item")
    val images: List<RecommendImage> = listOf()
)

data class RecommendImage(
    @SerializedName("originimgurl")
    val imageUrl: String,

    @SerializedName("serialnum")
    val imageId: String,
)
