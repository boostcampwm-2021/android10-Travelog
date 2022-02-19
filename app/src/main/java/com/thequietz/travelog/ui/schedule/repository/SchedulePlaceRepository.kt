package com.thequietz.travelog.ui.schedule.repository

import android.util.Log
import com.google.gson.JsonSyntaxException
import com.thequietz.travelog.api.PlaceService
import com.thequietz.travelog.ui.schedule.model.SchedulePlaceModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.awaitResponse
import javax.inject.Inject

class SchedulePlaceRepository @Inject constructor(
    private val placeService: PlaceService
) {
    private val TAG = "SCHEDULE_PLACE"
    private val emptyList = emptyList<SchedulePlaceModel>()

    suspend fun loadPlaceList(): List<SchedulePlaceModel> {
        return withContext(Dispatchers.IO) {
            try {
                val call =
                    placeService.loadPlaceList()
                val resp = call.awaitResponse()
                if (!resp.isSuccessful || resp.body() == null) {
                    Log.d(TAG, resp.message())
                    listOf<SchedulePlaceModel>()
                }
                resp.body()?.data ?: listOf()
            } catch (e: JsonSyntaxException) {
                Log.d(TAG, e.message.toString())
                emptyList
            } catch (t: Throwable) {
                Log.d(TAG, t.stackTraceToString())
                emptyList
            }
        }
    }

    suspend fun searchPlaceList(keyword: String): List<SchedulePlaceModel> {
        return withContext(Dispatchers.IO) {
            try {
                val call = placeService.searchPlaceList(keyword)
                val resp = call.awaitResponse()
                if (!resp.isSuccessful || resp.body() == null) {
                    Log.d(TAG, resp.errorBody().toString())
                }
                resp.body()?.data ?: emptyList
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
