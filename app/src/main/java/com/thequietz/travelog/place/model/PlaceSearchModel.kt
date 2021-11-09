package com.thequietz.travelog.place.model

import com.google.gson.annotations.SerializedName

data class PlaceSearchModelResponse(
    @SerializedName("results")
    val results: List<PlaceSearchModel>,
)

data class PlaceSearchModel(
    @SerializedName("name")
    val name: String,

    @SerializedName("place_id")
    val placeId: String,

    @SerializedName("geometry")
    val geometry: PlaceGeometry,
)

data class PlaceGeometry(
    @SerializedName("location")
    val location: PlaceLocation
)

data class PlaceLocation(
    @SerializedName("lat")
    val latitude: Double,

    @SerializedName("lng")
    val longitude: Double,
)
