package com.thequietz.travelog.guide

import androidx.room.Entity
import androidx.room.PrimaryKey
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
@Entity(tableName = "RecommendPlaceDB")
data class RecommendPlace(
    @SerializedName("title") val name: String = "",
    @SerializedName("firstimage") val url: String = "",
    @SerializedName("addr1") val description: String = "",
    @SerializedName("readcount") val readCount: String,
    @SerializedName("mapx") val longitude: Double = 0.0,
    @SerializedName("mapy") val latitude: Double = 0.0,
    @PrimaryKey @SerializedName("contentid") val contentId: Long,
    @SerializedName("contenttypeid") val contentTypeId: Int,
    @SerializedName("areacode") val areaCode: Int,
    @SerializedName("cat1") val category: String,
    @SerializedName("sigungucode") val sigunguCode: Int,
    val eventStartDate: String = ""
)
