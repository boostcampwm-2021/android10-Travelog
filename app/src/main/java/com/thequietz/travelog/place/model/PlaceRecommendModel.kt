package com.thequietz.travelog.place.model

import androidx.lifecycle.LiveData
import com.google.gson.annotations.SerializedName

data class PlaceRecommendResponse(
    @SerializedName("response")
    val response: PlaceRecommendBody,
)

data class PlaceRecommendBody(
    @SerializedName("body")
    val body: PlaceRecommendItems,
)

data class PlaceRecommendItems(
    @SerializedName("items")
    val items: PlaceRecommendItem,
)

data class PlaceRecommendItem(
    @SerializedName("item")
    val item: List<PlaceRecommendModel>
)

data class PlaceRecommendModel(
    @SerializedName("title")
    val name: String,

    @SerializedName("firstimage")
    val imageUrl: String,

    @SerializedName("addr1")
    val address: String,

    @SerializedName("mapy")
    val latitude: Double,

    @SerializedName("mapx")
    val longitude: Double,

    /*@SerializedName("zipcode")
    val zipCode: Int,*/

    @SerializedName("contentid")
    val contentId: Long,

    @SerializedName("contenttypeid")
    val contentTypeId: Int,
)

data class PlaceRecommendWithList(
    val title: String,
    val list: LiveData<List<PlaceRecommendModel>>
)
