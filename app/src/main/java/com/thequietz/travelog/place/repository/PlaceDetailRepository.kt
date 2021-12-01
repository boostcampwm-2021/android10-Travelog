package com.thequietz.travelog.place.repository

import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.thequietz.travelog.BuildConfig
import com.thequietz.travelog.api.PlaceRecommendService
import com.thequietz.travelog.api.PlaceSearchService
import com.thequietz.travelog.place.model.PlaceDetailModel
import com.thequietz.travelog.place.model.detail.RecommendImage
import com.thequietz.travelog.place.model.detail.RecommendImageItem
import com.thequietz.travelog.place.model.detail.RecommendImageObject
import com.thequietz.travelog.place.model.detail.RecommendInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.awaitResponse
import javax.inject.Inject

class PlaceDetailRepository @Inject constructor(
    private val searchService: PlaceSearchService,
    private val recommendService: PlaceRecommendService,
) {
    private val TAG = "PLACE_DETAIL"
    private val gson = Gson()
    private val emptyList = emptyList<RecommendImage>()

    suspend fun loadPlaceDetail(placeId: String): PlaceDetailModel? {
        return withContext(Dispatchers.IO) {
            val apiKey = BuildConfig.GOOGLE_MAP_KEY
            val call = searchService.loadPlaceDetail(placeId, "ko", apiKey)
            val resp = call.awaitResponse()
            if (!resp.isSuccessful || resp.body() == null) {
                Log.d(TAG, resp.errorBody().toString())
            }
            resp.body()?.result
        }
    }

    suspend fun loadPlaceInfo(typeId: Int, id: Long): RecommendInfo? {
        return withContext(Dispatchers.IO) {
            val apiKey = BuildConfig.TOUR_API_KEY
            val call = recommendService.loadPlaceInfo(typeId, id, apiKey)
            val response = call.awaitResponse()
            if (!response.isSuccessful || response.body() == null) {
                Log.d(TAG, response.errorBody().toString())
            }
            Log.d(TAG, response.body().toString())
            response.body()?.response?.body?.items?.info
        }
    }

    suspend fun loadPlaceImages(typeId: Int, id: Long): List<RecommendImage> {
        return withContext(Dispatchers.IO) {
            val jsonText = try {
                val apiKey = BuildConfig.TOUR_API_KEY
                val call = recommendService.loadPlaceImages(typeId, id, apiKey)
                val response = call.awaitResponse()
                if (!response.isSuccessful || response.body() == null) {
                    Log.d(TAG, response.errorBody().toString())
                }
                Log.d(TAG, response.body().toString())
                val items = response.body()?.response?.body?.items
                gson.toJson(items)
            } catch (t: Throwable) {
                Log.d(TAG, t.stackTraceToString())
                ""
            }

            try {
                gson.fromJson(jsonText, RecommendImageItem::class.java).images
            } catch (e: JsonSyntaxException) {
                try {
                    val images = gson.fromJson(jsonText, RecommendImageObject::class.java)
                    listOf(images.image)
                } catch (e: JsonSyntaxException) {
                    Log.d(TAG, e.toString())
                    emptyList
                }
            } catch (t: Throwable) {
                Log.d(TAG, t.stackTraceToString())
                emptyList
            }
        }
    }
}
