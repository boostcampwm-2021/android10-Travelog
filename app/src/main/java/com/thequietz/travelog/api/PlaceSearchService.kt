package com.thequietz.travelog.api

import com.thequietz.travelog.place.model.PlaceDetailModelResponse
import com.thequietz.travelog.place.model.PlaceSearchModelResponse
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface PlaceSearchService {

    @GET("maps/api/place/textsearch/json")
    fun loadPlaceList(
        @Query("query") query: String,
        @Query("language") language: String? = "ko",
        @Query("key") key: String
    ): Call<PlaceSearchModelResponse>

    @GET("maps/api/place/details/json")
    fun loadPlaceDetail(
        @Query("place_id") placeId: String,
        @Query("language") language: String? = "ko",
        @Query("key") key: String,
    ): Call<PlaceDetailModelResponse>

    companion object {
        private const val apiUrl = "https://maps.googleapis.com/"

        fun create(): PlaceSearchService {
            return Retrofit
                .Builder()
                .baseUrl(apiUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(PlaceSearchService::class.java)
        }
    }
}
