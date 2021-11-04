package com.thequietz.travelog

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiFactory {
    private const val TOUR_BASE_URL_AREA = "http://api.visitkorea.or.kr"
    private const val NEW_URL = "https://fk4zc712ck.execute-api.ap-northeast-2.amazonaws.com"
    fun createTourApi(): BaseApiContract.TourApi {
        val gson = GsonBuilder()
            .setLenient()
            .create()

        val retrofit = Retrofit.Builder()
            .baseUrl(TOUR_BASE_URL_AREA)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        return retrofit.create(BaseApiContract.TourApi::class.java)
    }
    fun createNewApi(): BaseApiContract.PlaceListApi {
        val gson = GsonBuilder()
            .setLenient()
            .create()

        val retrofit = Retrofit.Builder()
            .baseUrl(NEW_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        return retrofit.create(BaseApiContract.PlaceListApi::class.java)
    }
}
