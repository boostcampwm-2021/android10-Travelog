package com.thequietz.travelog.place.repository

import android.util.Log
import com.thequietz.travelog.BuildConfig
import com.thequietz.travelog.api.PlaceRecommendService
import com.thequietz.travelog.place.model.PlaceRecommendModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.awaitResponse
import javax.inject.Inject

class PlaceRecommendRepository @Inject constructor(
    private val service: PlaceRecommendService,
) {
    private val TAG = "PLACE_RECOMMEND"

    suspend fun loadPlaceData(typeId: Int): List<PlaceRecommendModel> {
        return withContext(Dispatchers.IO) {
            try {
                val apiKey = BuildConfig.TOUR_API_KEY
                val call = service.loadRecommendPlace(typeId, apiKey)
                val resp = call.awaitResponse()

                if (!resp.isSuccessful || resp.body() == null) {
                    Log.d(TAG, resp.errorBody().toString())
                    listOf<PlaceRecommendModel>()
                }
                val items = resp.body()?.response?.body?.items
                items?.item ?: listOf()
            } catch (e: HttpException) {
                listOf()
            }
        }
    }
}
