package com.thequietz.travelog.place.repository

import android.util.Log
import com.google.gson.JsonSyntaxException
import com.thequietz.travelog.BuildConfig
import com.thequietz.travelog.api.PlaceSearchService
import com.thequietz.travelog.place.model.PlaceSearchModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.awaitResponse
import java.net.SocketTimeoutException
import javax.inject.Inject

class PlaceSearchRepository @Inject constructor(
    private val service: PlaceSearchService,
) {
    private val TAG = "PLACE_SEARCH"
    private val emptyList = emptyList<PlaceSearchModel>()

    suspend fun loadPlaceList(query: String): List<PlaceSearchModel> {
        return withContext(Dispatchers.IO) {
            try {
                val apiKey = BuildConfig.GOOGLE_MAP_KEY
                val call = service.loadPlaceList(query, "ko", apiKey)
                val resp = call.awaitResponse()
                if (!resp.isSuccessful || resp.body() == null) {
                    Log.d(TAG, resp.errorBody().toString())
                    emptyList
                }
                resp.body()?.results ?: emptyList
            } catch (e: SocketTimeoutException) {
                Log.d(TAG, e.message.toString())
                emptyList
            } catch (e: JsonSyntaxException) {
                Log.d(TAG, e.message.toString())
                emptyList
            } catch (t: Throwable) {
                Log.d(TAG, t.stackTraceToString())
                emptyList
            }
        }
    }
}
