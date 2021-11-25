package com.thequietz.travelog.guide

import android.os.Parcelable
import androidx.room.Entity
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

data class AllPlaceResponse(
    @SerializedName("data") val data: List<Place>
)
@Parcelize
@Entity(tableName = "PlaceDB", primaryKeys = ["areaCode", "sigunguCode"])
data class Place(
    @SerializedName("firstimage") val url: String,
    @SerializedName("cityname") val name: String,
    @SerializedName("areacode") val areaCode: Int,
    @SerializedName("sigungucode") val sigunguCode: Int,
    @SerializedName("title") val title: String,
    @SerializedName("statename") val stateName: String
) : Serializable, Parcelable
