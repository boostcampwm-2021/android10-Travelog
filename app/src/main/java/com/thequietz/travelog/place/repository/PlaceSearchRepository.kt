package com.thequietz.travelog.place.repository

import android.util.Log
import com.thequietz.travelog.BuildConfig
import com.thequietz.travelog.api.PlaceSearchService
import com.thequietz.travelog.place.model.PlaceSearchModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.awaitResponse
import javax.inject.Inject

class PlaceSearchRepository @Inject constructor(
    private val service: PlaceSearchService,
) {
    private val TAG = "PLACE_SEARCH"

    suspend fun loadPlaceList(query: String): List<PlaceSearchModel> {
        return withContext(Dispatchers.IO) {
            try {
                val apiKey = BuildConfig.GOOGLE_MAP_KEY
                val call = service.loadPlaceList(query, apiKey)
                val resp = call.awaitResponse()
                if (!resp.isSuccessful || resp.body() == null) {
                    Log.d(TAG, resp.errorBody().toString())
                    listOf<PlaceSearchModel>()
                }
                resp.body()?.results ?: listOf()
            } catch (e: HttpException) {
                Log.d(TAG, e.message())
                listOf()
            }
        }
    }
}
