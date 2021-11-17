package com.thequietz.travelog.api

import com.thequietz.travelog.place.model.PlaceRecommendResponse
import com.thequietz.travelog.place.model.detail.RecommendImageResponse
import com.thequietz.travelog.place.model.detail.RecommendResponse
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

    @GET("openapi/service/rest/KorService/detailCommon")
    fun loadPlaceInfo(
        @Query("contentTypeId") typeId: Int,
        @Query("contentId") id: Long,
        @Query("ServiceKey") apiKey: String,
        @Query("MobileOS") os: String = "AND",
        @Query("MobileApp") appName: String = "Travelog",
        @Query("_type") contentType: String = "json",
        @Query("defaultYN") default: String = "Y",
        @Query("addrinfoYN") address: String = "Y",
        @Query("mapinfoYN") map: String = "Y",
        @Query("overviewYN") overview: String = "Y"
    ): Call<RecommendResponse>

    @GET("openapi/service/rest/KorService/detailImage")
    fun loadPlaceImages(
        @Query("contentTypeId") typeId: Int,
        @Query("contentId") id: Long,
        @Query("ServiceKey") apiKey: String,
        @Query("MobileOS") os: String = "AND",
        @Query("MobileApp") appName: String = "Travelog",
        @Query("_type") contentType: String = "json",
        @Query("numOfRows") rows: Int = 25,
    ): Call<RecommendImageResponse>

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
