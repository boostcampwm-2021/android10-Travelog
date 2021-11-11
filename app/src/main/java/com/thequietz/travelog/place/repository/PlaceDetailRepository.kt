package com.thequietz.travelog.place.repository

import com.thequietz.travelog.BuildConfig
import com.thequietz.travelog.api.PlaceSearchService
import com.thequietz.travelog.place.model.PlaceDetailModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.awaitResponse
import javax.inject.Inject

class PlaceDetailRepository @Inject constructor(
    private val service: PlaceSearchService
) {

    suspend fun loadPlaceDetail(placeId: String): PlaceDetailModel? {
        return withContext(Dispatchers.IO) {
            val apiKey = BuildConfig.GOOGLE_MAP_KEY
            val call = service.loadPlaceDetail(placeId, apiKey)
            val response = call.awaitResponse()
            if (!response.isSuccessful || response.body() == null) {
                null
            }

            response.body()?.result
        }
    }
}
