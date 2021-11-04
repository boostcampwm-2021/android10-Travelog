package com.thequietz.travelog.api

import com.thequietz.travelog.schedule.model.PlaceResponse
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface PlaceService {
    @GET("dev")
    fun loadPlaceList(): Call<PlaceResponse>

    @GET("dev/search/{keyword}")
    fun searchPlaceList(@Path("keyword") keyword: String): Call<PlaceResponse>

    companion object {
        private const val apiUrl = "https://fk4zc712ck.execute-api.ap-northeast-2.amazonaws.com/"

        fun create(): PlaceService {
            return Retrofit
                .Builder()
                .baseUrl(apiUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(PlaceService::class.java)
        }
    }
}
