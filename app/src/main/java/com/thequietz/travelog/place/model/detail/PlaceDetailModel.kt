package com.thequietz.travelog.place.model.detail

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class PlaceDetailImage(
    @SerializedName("photo_reference")
    val imageId: String,

    val imageUrl: String = "",
) : Parcelable

@Parcelize
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
) : Parcelable

@Parcelize
data class PlaceDetailLocation(
    @SerializedName("lat")
    val latitude: Double,

    @SerializedName("lon")
    val longitude: Double,
) : Parcelable

@Parcelize
data class PlaceDetailGeometry(
    @SerializedName("location")
    val location: PlaceDetailLocation
) : Parcelable

@Parcelize
data class PlaceDetailOperation(
    @SerializedName("open_now")
    val isOpened: Boolean,

    @SerializedName("weekday_text")
    val operation: List<String>,
) : Parcelable

@Parcelize
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
    val images: List<PlaceDetailImage>,

    @SerializedName("reviews")
    val reviews: List<PlaceDetailReview>?,

    @SerializedName("types")
    val types: List<String>,

    @SerializedName("website")
    val website: String,

    val overview: String? = ""
) : Parcelable

data class PlaceDetailModelResponse(
    @SerializedName("result")
    val result: PlaceDetailModel,

    @SerializedName("status")
    val status: String,
)
