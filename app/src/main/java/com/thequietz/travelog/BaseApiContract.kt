package com.thequietz.travelog

import com.thequietz.travelog.guide.AllPlaceResponse
import com.thequietz.travelog.guide.RecommendResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

const val SERVICE_KEY = "nCt96vygV7qQ2zPRVStQfojU6mqUXNHBQbnUhOIlRuPoc5xiVQXTzspBS9GGS8sv3yKlcY7SY6HJYJ4SFruRtA%3D%3D"

interface BaseApiContract {
    interface TourApi {
        @GET("/openapi/service/rest/KorService/areaBasedList?ServiceKey=$SERVICE_KEY&contentTypeid=15&MobileOS=AND&MobileApp=Testing&_type=json&arrange=P")
        suspend fun requestRecommendPlace(@Query("areaCode") area: String, @Query("sigunguCode") sigunguCode: String): RecommendResponse
    }

    interface PlaceListApi {
        @GET("/dev/{id}")
        suspend fun requestAllPlace(@Path("id") id: String): AllPlaceResponse
    }
}
