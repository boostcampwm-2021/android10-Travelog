package com.thequietz.travelog.data

import com.thequietz.travelog.guide.model.GuideResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface TravelAPI {
    @GET("areaBasedList")
    fun loadAreaList(
        @Query("numOfRows") num: String = "10",
        @Query("pageNo") page: String = "1",
        @Query("MobileOS") os: String = "ETC",
        @Query("MobileApp") app: String = "Travelog",
        @Query("ServiceKey") serviceKey: String,
        @Query("listYN") isList: String = "Y",
        @Query("arrange") arrange: String = "P",
        @Query("_type") responseType: String = "json",
        @Query("areaCode") code: String,
    ): Call<GuideResponse>
}
