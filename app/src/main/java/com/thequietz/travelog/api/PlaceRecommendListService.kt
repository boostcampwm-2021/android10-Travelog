package com.thequietz.travelog.api

import com.thequietz.travelog.ui.place.model.PlaceRecommendListResponse
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface PlaceRecommendListService {

    @GET("dev/recommend/{category}")
    fun loadPlaceList(@Path("category") category: String): Call<PlaceRecommendListResponse>

    companion object {
        private const val apiUrl = "https://fk4zc712ck.execute-api.ap-northeast-2.amazonaws.com/"

        fun create(): PlaceRecommendListService {
            return Retrofit.Builder()
                .baseUrl(apiUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(PlaceRecommendListService::class.java)
        }
    }
}
