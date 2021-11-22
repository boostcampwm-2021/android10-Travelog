package com.thequietz.travelog.schedule.repository

import android.util.Log
import com.google.gson.JsonSyntaxException
import com.thequietz.travelog.api.PlaceService
import com.thequietz.travelog.schedule.model.PlaceModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.awaitResponse
import javax.inject.Inject

class SchedulePlaceRepository @Inject constructor(
    private val placeService: PlaceService
) {
    private val TAG = "SCHEDULE_PLACE"

    suspend fun loadPlaceList(): List<PlaceModel> {
        return withContext(Dispatchers.IO) {
            try {
                val call =
                    placeService.loadPlaceList()
                val resp = call.awaitResponse()
                if (!resp.isSuccessful || resp.body() == null) {
                    Log.d(TAG, resp.message())
                    listOf<PlaceModel>()
                }
                resp.body()?.data ?: listOf()
            } catch (e: HttpException) {
                Log.d(TAG, e.message())
                listOf()
            } catch (e: JsonSyntaxException) {
                Log.d(TAG, e.message.toString())
                listOf()
            }
        }
    }

    suspend fun searchPlaceList(keyword: String): List<PlaceModel> {
        return withContext(Dispatchers.IO) {
            try {
                val call = placeService.searchPlaceList(keyword)
                val resp = call.awaitResponse()
                if (!resp.isSuccessful || resp.body() == null) {
                    Log.d(TAG, resp.errorBody().toString())
                }
                resp.body()?.data ?: listOf()
            } catch (e: HttpException) {
                Log.d(TAG, e.message())
                listOf()
            } catch (e: JsonSyntaxException) {
                Log.d(TAG, e.message.toString())
                listOf()
            }
        }
    }
}
