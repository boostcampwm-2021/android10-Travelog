package com.thequietz.travelog.schedule.repository

import com.thequietz.travelog.data.TravelAPI
import com.thequietz.travelog.schedule.model.SelectModel
import retrofit2.Retrofit
import retrofit2.awaitResponse
import retrofit2.converter.gson.GsonConverterFactory

class SelectRepository {
    private val apiUrl = "https://fk4zc712ck.execute-api.ap-northeast-2.amazonaws.com/"
    private val retrofit =
        Retrofit
            .Builder()
            .baseUrl(apiUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    private val selectService = retrofit.create(TravelAPI::class.java)

    suspend fun loadAreaList(): List<SelectModel> {
        val call =
            selectService.loadAreaList()
        val response = call.awaitResponse()
        if (!response.isSuccessful || response.body() == null) {
            return listOf()
        }
        val body = response.body()!!
        val items = body.data
        if (items.isEmpty()) return listOf()
        return items
    }
}
