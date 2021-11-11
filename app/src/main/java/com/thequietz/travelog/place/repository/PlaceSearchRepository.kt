package com.thequietz.travelog.place.repository

import com.thequietz.travelog.api.PlaceSearchService
import com.thequietz.travelog.place.model.PlaceSearchModel
import retrofit2.awaitResponse
import javax.inject.Inject

class PlaceSearchRepository @Inject constructor(
    private val service: PlaceSearchService,
) {
    suspend fun loadPlaceList(query: String): List<PlaceSearchModel> {
        val apiKey = "" // BuildConfig.GOOGLE_MAP_KEY
        val call = service.loadPlaceList(query, apiKey)
        val response = call.awaitResponse()
        if (!response.isSuccessful || response.body() == null) {
            return listOf()
        }
        return response.body()?.results ?: listOf()
    }
}
