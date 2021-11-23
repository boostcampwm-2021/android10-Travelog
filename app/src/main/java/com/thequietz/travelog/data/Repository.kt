package com.thequietz.travelog.data

import android.util.Log
import com.google.gson.JsonSyntaxException
import com.thequietz.travelog.BuildConfig
import com.thequietz.travelog.api.GuideRecommendService
import com.thequietz.travelog.api.PlaceService
import com.thequietz.travelog.data.db.dao.RecordImageDao
import com.thequietz.travelog.getTodayDate
import com.thequietz.travelog.guide.Place
import com.thequietz.travelog.guide.RecommendPlace
import com.thequietz.travelog.record.model.RecordImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GuideRepository @Inject constructor(
    private val placeService: PlaceService,
    private val guideRecommendService: GuideRecommendService
) {
    private val TAG = "GUIDE"
    private val TOUR_API_KEY = BuildConfig.TOUR_API_KEY
    private val NEW_TOUR_API_KEY = BuildConfig.NEW_TOUR_API_KEY

    private val emptyList = emptyList<Place>()
    private val emptyRecommendList = emptyList<RecommendPlace>()

    suspend fun loadAllPlaceData(): List<Place> {
        return try {
            placeService.requestAll().data
        } catch (e: JsonSyntaxException) {
            emptyList
        } catch (t: Throwable) {
            Log.d(TAG, t.stackTraceToString())
            emptyList
        }
    }

    suspend fun loadAllDoSi(): List<Place> {
        return try {
            val result = placeService.requestALLDoSi()
            result.data
        } catch (e: JsonSyntaxException) {
            emptyList
        } catch (t: Throwable) {
            Log.d(TAG, t.stackTraceToString())
            emptyList
        }
    }

    suspend fun loadDoSiByCode(code: String = "3"): List<Place> {
        return try {
            val result = placeService.requestDoSiByCode(code)
            result.data
        } catch (e: JsonSyntaxException) {
            emptyList
        } catch (t: Throwable) {
            Log.d(TAG, t.stackTraceToString())
            emptyList
        }
    }

    suspend fun loadDoSiByKeyword(keyword: String = "서"): List<Place> {
        return try {
            val result = placeService.requestAllByKeyword(keyword)
            result.data
        } catch (e: JsonSyntaxException) {
            emptyList
        } catch (t: Throwable) {
            Log.d(TAG, t.stackTraceToString())
            emptyList
        }
    }

    suspend fun loadRecommendPlaceData(
        areaCode: String = "1",
        sigunguCode: String = "10"
    ): List<RecommendPlace> {
        return try {
            val res = guideRecommendService.requestRecommendPlace(
                areaCode,
                sigunguCode,
                TOUR_API_KEY
            ).response.body.items.item
            res
        } catch (e: JsonSyntaxException) {
            emptyRecommendList
        } catch (t: Throwable) {
            Log.d(TAG, t.stackTraceToString())
            emptyRecommendList
        }
    }

    suspend fun loadAreaData(
        areaCode: String = "1",
        requestType: String = "A01",
        pageNo: Int
    ): List<RecommendPlace> {
        return try {
            val res = guideRecommendService.requestAreaBased(TOUR_API_KEY, areaCode, requestType, pageNo)
            val maxPage = (res.response.body.totalCnt / 10) + 1
            if (maxPage < pageNo) {
                emptyRecommendList
            } else {
                res.response.body.items.item
            }
        } catch (e: JsonSyntaxException) {
            emptyRecommendList
        } catch (t: Throwable) {
            Log.d(TAG, t.stackTraceToString())
            emptyRecommendList
        }
    }

    /*suspend fun loadVacationSpotData(areaCode: String): List<RecommendPlace> {
        val res = placeRecommend.requestVacationSpot(areaCode).response.body.items.item
        return res
    }

    suspend fun loadFoodData(areaCode: String): List<RecommendPlace> {
        val res = placeRecommend.requestFood(areaCode).response.body.items.item
        return res
    }
*/
    suspend fun loadFestivalData(areaCode: String, pageNo: Int): List<RecommendPlace> {
        return try {
            val res = guideRecommendService.requestFestival(TOUR_API_KEY, getTodayDate(), areaCode, pageNo)
            val maxPage = (res.response.body.totalCnt / 10) + 1
            if (maxPage < pageNo) {
                emptyRecommendList
            } else {
                res.response.body.items.item
            }
        } catch (e: JsonSyntaxException) {
            emptyRecommendList
        } catch (t: Throwable) {
            Log.d(TAG, t.stackTraceToString())
            emptyRecommendList
        }
    }
}

@Singleton
class RecordRepository @Inject constructor(
    private val recordImageDao: RecordImageDao,
    private val coroutineScope: CoroutineScope
) {
    fun loadRecordImages() = recordImageDao.loadAllRecordImages()

    fun loadRecordImagesByTitle(title: String) = recordImageDao.loadRecordImageByTitle(title)

    fun createRecordImage(recordImage: RecordImage) {
        coroutineScope.launch { recordImageDao.insert(recordImage) }
    }

    fun updateRecordImageComment(comment: String, id: Int) {
        coroutineScope.launch { recordImageDao.updateRecordImageCommentById(comment, id) }
    }

    fun insertRecordImage(image: RecordImage) {
        coroutineScope.launch { recordImageDao.insert(image) }
    }

    fun deleteRecordImage(id: Int) {
        coroutineScope.launch {
            val data = recordImageDao.loadRecordImageById(id)
            if (data != null) {
                recordImageDao.delete(data)
            }
        }
    }

    fun deleteRecordImageByPlace(place: String) {
        coroutineScope.launch {
            recordImageDao.deleteRecordImageByPlace(place)
        }
    }
}
