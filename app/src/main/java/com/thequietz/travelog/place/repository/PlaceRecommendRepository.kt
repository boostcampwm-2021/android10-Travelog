package com.thequietz.travelog.place.repository

import com.thequietz.travelog.BuildConfig
import com.thequietz.travelog.api.PlaceRecommendService
import com.thequietz.travelog.place.model.PlaceRecommendModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.awaitResponse
import javax.inject.Inject

class PlaceRecommendRepository @Inject constructor(
    private val service: PlaceRecommendService,
) {
    suspend fun loadPlaceData(typeId: Int): List<PlaceRecommendModel> {
        return withContext(Dispatchers.IO) {
            val apiKey = BuildConfig.TOUR_API_KEY
            val call = service.loadRecommendPlace(typeId, apiKey)
            val resp = call.awaitResponse()
            if (!resp.isSuccessful || resp.body() == null) {
                listOf<PlaceRecommendModel>()
            }
            resp.body()?.response?.body?.items?.items ?: listOf()
        }
    }
}
