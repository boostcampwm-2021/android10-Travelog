package com.thequietz.travelog.api

import com.thequietz.travelog.guide.RecommendResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface GuideRecommendService {
    @GET("/openapi/service/rest/KorService/areaBasedList")
    suspend fun requestRecommendPlace(
        @Query("areaCode") area: String,
        @Query("sigunguCode") secondCode: String,
        @Query("ServiceKey") key: String,
        @Query("pageNo") pageNo: Int,
        @Query("contentTypeId") typeId: Int = 14,
        @Query("MobileOS") os: String = "AND",
        @Query("MobileApp") appName: String = "Travelog",
        @Query("_type") contentType: String = "json",
        @Query("arrange") arrange: String = "P"
    ): RecommendResponse

    @GET("/openapi/service/rest/KorService/areaBasedList")
    suspend fun requestAreaBased(
        @Query("ServiceKey") key: String,
        @Query("areaCode") code: String,
        @Query("cat1") category: String,
        @Query("pageNo") pageNo: Int,
        @Query("MobileOS") os: String = "AND",
        @Query("MobileApp") appName: String = "Travelog",
        @Query("_type") contentType: String = "json",
        @Query("arrange") arrange: String = "P",
    ): RecommendResponse

    /* @GET("/openapi/service/rest/KorService/areaBasedList?ServiceKey=$SERVICE_KEY&MobileOS=AND&MobileApp=Travlelog&cat1=A01&_type=json&arrange=P")
     suspend fun requestVacationSpot(@Query("areaCode") code: String): RecommendResponse

     @GET("/openapi/service/rest/KorService/areaBasedList?ServiceKey=$SERVICE_KEY&MobileOS=AND&MobileApp=Travlelog&cat1=A03&_type=json&arrange=P")
     suspend fun requestSports(@Query("areaCode") code: String): RecommendResponse

     @GET("/openapi/service/rest/KorService/areaBasedList?ServiceKey=$SERVICE_KEY&MobileOS=AND&MobileApp=Travlelog&cat1=A05&_type=json&arrange=P")
     suspend fun requestFood(@Query("areaCode") code: String): RecommendResponse*/

    @GET("/openapi/service/rest/KorService/searchFestival")
    suspend fun requestFestival(
        @Query("ServiceKey") key: String,
        @Query("eventStartDate") startDate: String,
        @Query("areaCode") code: String,
        @Query("pageNo") pageNo: Int,
        @Query("MobileOS") os: String = "AND",
        @Query("MobileApp") appName: String = "Travelog",
        @Query("_type") contentType: String = "json",
        @Query("arrange") arrange: String = "P",
    ): RecommendResponse

    companion object {
        private const val apiUrl = "http://api.visitkorea.or.kr"

        fun create(): GuideRecommendService {
            return Retrofit
                .Builder()
                .baseUrl(apiUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(GuideRecommendService::class.java)
        }
    }
}
