package com.thequietz.travelog.data

import com.thequietz.travelog.guide.model.GuideResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface TravelAPI {
    @GET("areaBasedList")
    suspend fun loadAreaList(
        @Query("numOfRows") num: Int = 10,
        @Query("pageNo") page: Int = 1,
        @Query("MobileOS") os: String = "AND",
        @Query("MobileApp") app: String = "Travelog",
        @Query("ServiceKey") serviceKey: String,
        @Query("listYN") isList: String = "Y",
        @Query("arrange") arrange: String = "B",
        @Query("areaCode") code: Int,
    ): Call<GuideResponse>
}