package com.thequietz.travelog.place.repository

import android.util.Log
import com.thequietz.travelog.BuildConfig
import com.thequietz.travelog.api.PlaceRecommendService
import com.thequietz.travelog.api.PlaceSearchService
import com.thequietz.travelog.place.model.detail.PlaceDetailModel
import com.thequietz.travelog.place.model.detail.RecommendImage
import com.thequietz.travelog.place.model.detail.RecommendInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import retrofit2.awaitResponse
import java.lang.Exception
import javax.inject.Inject

class PlaceDetailRepository @Inject constructor(
    private val searchService: PlaceSearchService,
    private val recommendService: PlaceRecommendService,
) {
    private val TAG = "PLACE_DETAIL_REPOSITORY"
    private fun logIfError(msg: ResponseBody?) =
        Log.e(TAG, msg.toString())

    suspend fun loadPlaceDetail(placeId: String): PlaceDetailModel? {
        return withContext(Dispatchers.IO) {
            try {
                val apiKey = BuildConfig.GOOGLE_MAP_KEY
                val call = searchService.loadPlaceDetail(placeId, apiKey)
                val response = call.awaitResponse()
                if (!response.isSuccessful || response.body() == null) {
                    logIfError(response.errorBody())
                }
                response.body()?.result
            } catch (e: Exception) {
                Log.d(TAG, e.message.toString())
                null
            }
        }
    }

    suspend fun loadPlaceInfo(typeId: Int, id: Long): RecommendInfo? {
        return withContext(Dispatchers.IO) {
            val apiKey = BuildConfig.TOUR_API_KEY
            val call = recommendService.loadPlaceInfo(typeId, id, apiKey)
            val response = call.awaitResponse()
            if (!response.isSuccessful || response.body() == null) {
                logIfError(response.errorBody())
                null
            }
            response.body()?.response?.body?.items?.info
        }
    }

    suspend fun loadPlaceImages(typeId: Int, id: Long): List<RecommendImage>? {
        return withContext(Dispatchers.IO) {
            try {
                val apiKey = BuildConfig.TOUR_API_KEY
                val call = recommendService.loadPlaceImages(typeId, id, apiKey)
                val response = call.awaitResponse()
                if (!response.isSuccessful || response.body() == null) {
                    logIfError(response.errorBody())
                    null
                }
                response.body()?.response?.body?.items?.images
            } catch (e: Exception) {
                null
            }
        }
    }
}
