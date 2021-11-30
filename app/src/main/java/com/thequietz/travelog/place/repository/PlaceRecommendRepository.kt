package com.thequietz.travelog.place.repository

import android.util.Log
import com.thequietz.travelog.api.PlaceRecommendListService
import com.thequietz.travelog.place.model.PlaceRecommendModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.awaitResponse
import javax.inject.Inject

class PlaceRecommendRepository @Inject constructor(
    private val service: PlaceRecommendListService,
) {
    private val TAG = "PLACE_RECOMMEND"
    private var emptyList = emptyList<PlaceRecommendModel>()

    suspend fun loadPlaceData(category: String): List<PlaceRecommendModel> {
        return withContext(Dispatchers.IO) {
            val call = service.loadPlaceList(category)
            val resp = call.awaitResponse()

            if (!resp.isSuccessful || resp.body() == null) {
                Log.d(TAG, resp.errorBody().toString())
                emptyList
            }
            resp.body()?.data ?: emptyList
        }
    }
}
