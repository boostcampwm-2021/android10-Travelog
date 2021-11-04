package com.thequietz.travelog.schedule.repository

import com.thequietz.travelog.api.PlaceService
import com.thequietz.travelog.schedule.model.PlaceModel
import retrofit2.awaitResponse
import javax.inject.Inject

class SchedulePlaceRepository @Inject constructor(
    private val placeService: PlaceService
) {
    suspend fun loadPlaceList(): List<PlaceModel> {
        val call =
            placeService.loadPlaceList()
        val response = call.awaitResponse()
        if (!response.isSuccessful || response.body() == null) {
            return listOf()
        }
        val body = response.body()!!
        val items = body.data
        if (items.isEmpty()) return listOf()
        return items
    }

    suspend fun searchPlaceList(keyword: String): List<PlaceModel> {
        val call = placeService.searchPlaceList(keyword)
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
