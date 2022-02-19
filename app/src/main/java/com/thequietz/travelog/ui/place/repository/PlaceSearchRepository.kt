package com.thequietz.travelog.ui.place.repository

import android.util.Log
import com.thequietz.travelog.BuildConfig
import com.thequietz.travelog.api.PlaceSearchService
import com.thequietz.travelog.ui.place.model.PlaceSearchModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.awaitResponse
import javax.inject.Inject

class PlaceSearchRepository @Inject constructor(
    private val service: PlaceSearchService,
) {
    private val TAG = "PLACE_SEARCH"
    private val emptyList = emptyList<PlaceSearchModel>()

    suspend fun loadPlaceList(query: String, lat: Double, lng: Double): List<PlaceSearchModel> {
        return withContext(Dispatchers.IO) {
            val apiKey = BuildConfig.GOOGLE_MAP_KEY
            val location = "$lat,$lng"
            val call = service.loadPlaceList(query, location, "ko", apiKey)
            val resp = call.awaitResponse()
            if (!resp.isSuccessful || resp.body() == null) {
                Log.d(TAG, resp.errorBody().toString())
                emptyList
            }
            resp.body()?.results ?: emptyList
        }
    }
}
