package com.thequietz.travelog.data

import com.thequietz.travelog.schedule.model.SelectResponse
import retrofit2.Call
import retrofit2.http.GET

interface TravelAPI {
    @GET("dev")
    fun loadAreaList(): Call<SelectResponse>
}
