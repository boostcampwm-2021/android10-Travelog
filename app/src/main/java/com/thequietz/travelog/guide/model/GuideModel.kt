package com.thequietz.travelog.guide.model

import com.google.gson.annotations.SerializedName

data class GuideResponse(
    val response: GuideBody
)

data class GuideBody(
    val body: GuideItems,
)

data class GuideItems(
    val items: GuideItem,
)

data class GuideItem(
    val item: List<GuideModel>,
)

data class GuideModel(
    @SerializedName("firstimage")
    val thumbnail: String,

    @SerializedName("areacode")
    val areaCode: Int,

    var areaName: String? = ""
)

data class GuideName(
    val areaCode: Int,
    val areaName: String,
)