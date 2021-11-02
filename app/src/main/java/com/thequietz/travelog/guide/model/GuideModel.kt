package com.thequietz.travelog.guide.model

import com.google.gson.annotations.SerializedName

data class GuideResponse(
    @SerializedName("response")
    val response: GuideBody
)

data class GuideHeader(
    @SerializedName("ResultCode")
    val resultCode: String,

    @SerializedName("ResultMsg")
    val resultMessage: String,
)

data class GuideBody(
    @SerializedName("body")
    val body: GuideItems,

    @SerializedName("header")
    val header: GuideHeader
)

data class GuideItems(
    @SerializedName("items")
    val items: GuideItem,
)

data class GuideItem(
    @SerializedName("item")
    val item: List<GuideModel>,
)

data class GuideModel(
    @SerializedName("firstimage")
    var thumbnail: String,

    @SerializedName("areacode")
    val areaCode: Int,

    var areaName: String = ""
)

data class GuideName(
    val areaCode: Int,
    val areaName: String,
)
