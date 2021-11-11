package com.thequietz.travelog.place.model

import com.google.gson.annotations.SerializedName

data class PlaceDetailPhoto(
    @SerializedName("photo_reference")
    val photoId: String,
)

data class PlaceDetailReview(
    @SerializedName("author_name")
    val author: String,
    @SerializedName("profile_photo_url")
    val authorImage: String,
    @SerializedName("rating")
    val rating: Int,
    @SerializedName("relative_time_description")
    val timeDescription: String,
    @SerializedName("text")
    val text: String,
    @SerializedName("time")
    val time: Long,
)

data class PlaceDetailLocation(
    @SerializedName("lat")
    val latitude: Double,

    @SerializedName("lon")
    val longitude: Double,
)

data class PlaceDetailGeometry(
    @SerializedName("location")
    val location: PlaceDetailLocation
)

data class PlaceDetailOperation(
    @SerializedName("open_now")
    val isOpened: Boolean,

    @SerializedName("weekday_text")
    val operation: List<String>,
)

data class PlaceDetailModel(
    @SerializedName("place_id")
    val placeId: String,

    @SerializedName("formatted_address")
    val address: String,

    @SerializedName("formatted_phone_number")
    val phoneNumber: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("opening_hours")
    val operation: PlaceDetailOperation,

    @SerializedName("geometry")
    val geometry: PlaceDetailGeometry,

    @SerializedName("photos")
    val photos: List<PlaceDetailPhoto>,

    @SerializedName("reviews")
    val reviews: List<PlaceDetailReview>,

    @SerializedName("types")
    val types: List<String>,

    @SerializedName("website")
    val website: String,
)

data class PlaceDetailModelResponse(
    @SerializedName("result")
    val result: PlaceDetailModel,

    @SerializedName("status")
    val status: String,
)
