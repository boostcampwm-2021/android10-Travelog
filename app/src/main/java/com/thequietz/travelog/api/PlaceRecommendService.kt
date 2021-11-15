package com.thequietz.travelog.api

import com.thequietz.travelog.place.model.PlaceRecommendResponse
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface PlaceRecommendService {

    @GET("openapi/service/rest/KorService/areaBasedList")
    fun loadRecommendPlace(
        @Query("contentTypeId") typeId: Int,
        @Query("ServiceKey") apiKey: String,
        @Query("listYN") isList: String = "Y",
        @Query("MobileOS") os: String = "AND",
        @Query("MobileApp") appName: String = "Travelog",
        @Query("_type") contentType: String = "json",
        @Query("arrange") arrange: String = "Q",
        @Query("numOfRows") rows: Int = 20,
    ): Call<PlaceRecommendResponse>

    companion object {

        private const val apiUrl = "http://api.visitkorea.or.kr/"

        fun create(): PlaceRecommendService {
            return Retrofit.Builder()
                .baseUrl(apiUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(PlaceRecommendService::class.java)
        }
    }
}
