package com.thequietz.travelog.guide

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

data class AllPlaceResponse(
    @SerializedName("data") val data: List<Place>
)
@Parcelize
data class Place(
    @SerializedName("firstimage") val url: String,
    @SerializedName("cityname") val name: String,
    @SerializedName("areacode") val areaCode: String,
    @SerializedName("sigungucode") val sigunguCode: String,
    @SerializedName("title") val title: String,
    @SerializedName("statename") val stateName: String,
    val byteArray: ByteArray? = null
) : Serializable, Parcelable
