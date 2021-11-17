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
import retrofit2.HttpException
import retrofit2.awaitResponse
import javax.inject.Inject

class PlaceDetailRepository @Inject constructor(
    private val searchService: PlaceSearchService,
    private val recommendService: PlaceRecommendService,
) {
    private val TAG = "PLACE_DETAIL"

    suspend fun loadPlaceDetail(placeId: String): PlaceDetailModel? {
        return withContext(Dispatchers.IO) {
            try {
                val apiKey = BuildConfig.GOOGLE_MAP_KEY
                val call = searchService.loadPlaceDetail(placeId, apiKey)
                val resp = call.awaitResponse()
                if (!resp.isSuccessful || resp.body() == null) {
                    Log.d(TAG, resp.errorBody().toString())
                }
                resp.body()?.result
            } catch (e: HttpException) {
                Log.d(TAG, e.message())
                null
            }
        }
    }

    suspend fun loadPlaceInfo(typeId: Int, id: Long): RecommendInfo? {
        return withContext(Dispatchers.IO) {
            try {
                val apiKey = BuildConfig.TOUR_API_KEY
                val call = recommendService.loadPlaceInfo(typeId, id, apiKey)
                val response = call.awaitResponse()
                if (!response.isSuccessful || response.body() == null) {
                    Log.d(TAG, response.errorBody().toString())
                }
                response.body()?.response?.body?.items?.info
            } catch (e: HttpException) {
                Log.d(TAG, e.message())
                null
            }
        }
    }

    suspend fun loadPlaceImages(typeId: Int, id: Long): List<RecommendImage>? {
        return withContext(Dispatchers.IO) {
            try {
                val apiKey = BuildConfig.TOUR_API_KEY
                val call = recommendService.loadPlaceImages(typeId, id, apiKey)
                val response = call.awaitResponse()
                if (!response.isSuccessful || response.body() == null) {
                    Log.d(TAG, response.errorBody().toString())
                }
                response.body()?.response?.body?.items?.images
            } catch (e: HttpException) {
                Log.d(TAG, e.message())
                null
            }
        }
    }
}
