package com.thequietz.travelog.api

import com.thequietz.travelog.guide.RecommendResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

const val SERVICE_KEY =
    "nCt96vygV7qQ2zPRVStQfojU6mqUXNHBQbnUhOIlRuPoc5xiVQXTzspBS9GGS8sv3yKlcY7SY6HJYJ4SFruRtA%3D%3D"
const val NEW_SERVICE_KEY =
    "I2sJCPNlcsGIrgvauH84inSBsYebWCZnQpOVmzsR0bn%2B%2BuxZH%2Fqvq91QvQ02xehxqC6OBdprA1b0CVkrGxjOWw%3D%3D"

interface PlaceRecommend {
    @GET("/openapi/service/rest/KorService/areaBasedList?ServiceKey=$NEW_SERVICE_KEY&contentTypeId=15&MobileOS=AND&MobileApp=Travlelog&_type=json&arrange=P")
    suspend fun requestRecommendPlace(
        @Query("areaCode") area: String,
        @Query("sigunguCode") sigunguCode: String
    ): RecommendResponse

    @GET("/openapi/service/rest/KorService/areaBasedList?ServiceKey=$NEW_SERVICE_KEY&MobileOS=AND&MobileApp=Travlelog&_type=json&arrange=P")
    suspend fun requestAreaBased(
        @Query("areaCode") code: String,
        @Query("cat1") category: String,
        @Query("pageNo") pageNo: Int
    ): RecommendResponse

    /* @GET("/openapi/service/rest/KorService/areaBasedList?ServiceKey=$SERVICE_KEY&MobileOS=AND&MobileApp=Travlelog&cat1=A01&_type=json&arrange=P")
     suspend fun requestVacationSpot(@Query("areaCode") code: String): RecommendResponse

     @GET("/openapi/service/rest/KorService/areaBasedList?ServiceKey=$SERVICE_KEY&MobileOS=AND&MobileApp=Travlelog&cat1=A03&_type=json&arrange=P")
     suspend fun requestSports(@Query("areaCode") code: String): RecommendResponse

     @GET("/openapi/service/rest/KorService/areaBasedList?ServiceKey=$SERVICE_KEY&MobileOS=AND&MobileApp=Travlelog&cat1=A05&_type=json&arrange=P")
     suspend fun requestFood(@Query("areaCode") code: String): RecommendResponse*/

    @GET("/openapi/service/rest/KorService/searchFestival?ServiceKey=$NEW_SERVICE_KEY&MobileOS=ETC&MobileApp=Travlelog&_type=json&arrange=P")
    suspend fun requestFestival(
        @Query("eventStartDate") startDate: String,
        @Query("areaCode") code: String,
        @Query("pageNo") pageNo: Int
    ): RecommendResponse

    companion object {
        private const val apiUrl = "http://api.visitkorea.or.kr"

        fun create(): PlaceRecommend {
            return Retrofit
                .Builder()
                .baseUrl(apiUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(PlaceRecommend::class.java)
        }
    }
}
