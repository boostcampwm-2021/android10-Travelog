package com.thequietz.travelog.data

import android.util.Log
import com.google.gson.JsonSyntaxException
import com.thequietz.travelog.BuildConfig
import com.thequietz.travelog.api.GuideRecommendService
import com.thequietz.travelog.api.PlaceService
import com.thequietz.travelog.data.db.dao.JoinRecordDao
import com.thequietz.travelog.data.db.dao.NewRecordImage
import com.thequietz.travelog.data.db.dao.NewRecordImageDao
import com.thequietz.travelog.data.db.dao.PlaceDao
import com.thequietz.travelog.data.db.dao.RecommendPlaceDao
import com.thequietz.travelog.data.db.dao.RecordImageDao
import com.thequietz.travelog.util.getTodayDate
import com.thequietz.travelog.ui.guide.Place
import com.thequietz.travelog.ui.guide.RecommendPlace
import com.thequietz.travelog.ui.record.model.RecordImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GuideRepository @Inject constructor(
    private val placeService: PlaceService,
    private val guideRecommendService: GuideRecommendService,
    private val placeDao: PlaceDao,
    private val recommendPlaceDao: RecommendPlaceDao,
    private val coroutineScope: CoroutineScope
) {
    private val TAG = "GUIDE"
    private val NEW_TOUR_API_KEY = BuildConfig.TOUR_API_KEY
    private val TOUR_API_KEY = BuildConfig.NEW_TOUR_API_KEY

    private val emptyList = emptyList<Place>()
    private val emptyRecommendList = emptyList<RecommendPlace>()

    suspend fun loadAllPlaceData(): List<Place> {
        val loadData = placeDao.loadAllPlace()
        return if (loadData.isEmpty()) {
            try {
                val dosiRes = placeService.requestALLDoSi().data
                val allRes = placeService.requestAll().data
                coroutineScope.launch {
                    allRes.forEach {
                        placeDao.insert(
                            it.copy(
                                stateName = ""
                            )
                        )
                    }
                    dosiRes.forEach {
                        placeDao.insert(it)
                    }
                }
                allRes
            } catch (e: JsonSyntaxException) {
                emptyList
            } catch (t: Throwable) {
                Log.d(TAG, t.stackTraceToString())
                emptyList
            }
        } else {
            loadData
        }
    }

    suspend fun loadAllDoSi(): List<Place> {
        val loadData = placeDao.loadAllDOSI()
        return if (loadData.isEmpty()) {
            try {
                val res = placeService.requestALLDoSi().data
                res
            } catch (e: JsonSyntaxException) {
                emptyList
            } catch (t: Throwable) {
                Log.d(TAG, t.stackTraceToString())
                emptyList
            }
        } else {
            loadData
        }
    }

    suspend fun loadDoSiByCode(code: String = "3"): List<Place> {
        val loadData = placeDao.loadAllPlaceByAreaCode(code)
        return if (loadData.isEmpty()) {
            try {
                val res = placeService.requestDoSiByCode(code).data
                coroutineScope.launch {
                    res.forEach {
                        placeDao.insert(it)
                    }
                }
                res
            } catch (e: JsonSyntaxException) {
                emptyList
            } catch (t: Throwable) {
                Log.d(TAG, t.stackTraceToString())
                emptyList
            }
        } else {
            loadData
        }
    }

    suspend fun loadDoSiByKeyword(keyword: String = "서"): List<Place> {
        val loadData = placeDao.loadAllPlaceByKeyWord("str%")
        return if (loadData.isEmpty()) {
            try {
                val res = placeService.requestAllByKeyword(keyword).data
                res
            } catch (e: JsonSyntaxException) {
                emptyList
            } catch (t: Throwable) {
                Log.d(TAG, t.stackTraceToString())
                emptyList
            }
        } else {
            loadData
        }
    }

    suspend fun loadRecommendPlaceData(
        areaCode: String = "1",
        sigunguCode: String = "10",
        pageNo: Int = 1
    ): List<RecommendPlace> {
        val loadData =
            recommendPlaceDao.loadRecommendPlaceByAreaCodeAndSigunguCode(areaCode, sigunguCode)
        return if (loadData.isEmpty()) {
            try {
                val res = guideRecommendService.requestRecommendPlace(
                    areaCode,
                    sigunguCode,
                    NEW_TOUR_API_KEY,
                    pageNo
                ).response.body.items.item
                coroutineScope.launch {
                    res.forEach {
                        if (it.eventStartDate == null) {
                            recommendPlaceDao.insert(
                                it.copy(
                                    eventStartDate = ""
                                )
                            )
                        } else {
                            recommendPlaceDao.insert(it)
                        }
                    }
                }
                res
            } catch (e: JsonSyntaxException) {
                emptyRecommendList
            } catch (t: Throwable) {
                Log.d(TAG, t.stackTraceToString())
                emptyRecommendList
            }
        } else {
            loadData
        }
    }

    suspend fun loadAreaData(
        areaCode: String = "1",
        requestType: String = "A01",
        pageNo: Int
    ): List<RecommendPlace> {
        val loadData =
            recommendPlaceDao.loadRecommendPlaceByAreaCodeAndCategory(areaCode, requestType)
        if (loadData.size < pageNo * 10) {
            try {
                val res = guideRecommendService.requestAreaBased(
                    NEW_TOUR_API_KEY,
                    areaCode,
                    requestType,
                    pageNo
                )
                val maxPage = (res.response.body.totalCnt / 10) + 1
                return if (maxPage < pageNo) {
                    emptyRecommendList
                } else {
                    val resList = res.response.body.items.item.filter { it.url != null }
                    CoroutineScope(Dispatchers.IO).launch {
                        resList.forEach {
                            recommendPlaceDao.insert(
                                it.copy(
                                    eventStartDate = ""
                                )
                            )
                        }
                    }
                    resList
                }
            } catch (e: JsonSyntaxException) {
                return emptyRecommendList
            } catch (t: Throwable) {
                Log.d(TAG, t.stackTraceToString())
                return emptyRecommendList
            }
        } else {
            return loadData
        }
    }

    suspend fun loadFestivalData(areaCode: String, contentTypeId: Int, pageNo: Int): List<RecommendPlace> {
        val loadData =
            recommendPlaceDao.loadRecommendFestivalByAreaCode(areaCode, contentTypeId)
        if (loadData.size < pageNo * 10) {
            try {
                val res = guideRecommendService.requestFestival(
                    NEW_TOUR_API_KEY,
                    getTodayDate(),
                    areaCode,
                    pageNo
                )
                val maxPage = (res.response.body.totalCnt / 10) + 1
                return if (maxPage < pageNo) {
                    emptyRecommendList
                } else {
                    val resList = res.response.body.items.item.filter { it.url != null }
                    resList.forEach { println(it.toString()) }
                    coroutineScope.launch {
                        resList.forEach {
                            if (it.eventStartDate == null) {
                                recommendPlaceDao.insert(
                                    it.copy(
                                        contentTypeId = contentTypeId,
                                        eventStartDate = "",
                                        readCount = it.readCount ?: "0"
                                    )
                                )
                            } else {
                                recommendPlaceDao.insert(it)
                            }
                        }
                    }
                    resList
                }
            } catch (e: JsonSyntaxException) {
                return emptyRecommendList
            } catch (t: Throwable) {
                Log.d(TAG, t.stackTraceToString())
                return emptyRecommendList
            }
        } else {
            return loadData
        }
    }
}

@Singleton
class RecordRepository @Inject constructor(
    private val recordImageDao: RecordImageDao,
    private val newRecordImageDao: NewRecordImageDao,
    private val joinRecordDao: JoinRecordDao,
    private val coroutineScope: CoroutineScope
) {
    fun loadAnyJoinedRecordByTravelIdAndPlaceAndDay(travelId: Int, day: String, place: String) =
        joinRecordDao.loadStartJoinedRecordByTravelIdAndDayAndPlace(travelId, day, place, true, 1, 0)

    fun loadDefaultJoinedRecordByTravelId(travelId: Int, place: String) =
        joinRecordDao.loadDefaultJoinedRecordByTravelId(travelId, place)

    fun loadJoinedRecordByTravelIdAndPlace(travelId: Int, place: String) =
        joinRecordDao.loadJoinedRecordByTravelIdAndPlace(travelId, place)

    fun loadAnyJoinedrecordByTravelId(travelId: Int) =
        joinRecordDao.loadAnyJoinedRecordByTravelIdAndPlace(travelId)

    fun loadJoinedAll(travelId: Int) =
        joinRecordDao.loadAllJoined(travelId)

    fun loadAllIncludeDefault(travelId: Int) =
        joinRecordDao.loadJoinedRecordByTravelIdIncludeDefault(travelId)

    fun loadAll(travelId: Int) =
        joinRecordDao.loadJoinedRecordByTravelId(travelId)

    fun loadAllNewRecords() =
        newRecordImageDao.loadAllNewRecordImages()

    fun loadNewRecordImagesByTravelId(travelId: Int) =
        newRecordImageDao.loadNewRecordImagesByTravelId(travelId)

    fun loadDistinctJoinedRecordByTravelId(travelId: Int) =
        joinRecordDao.loadDistinctPlaceAndDayFromJoinedRecordByTravelId(travelId)

    fun loadRecordImagesByTravelId(travelId: Int) =
        recordImageDao.loadRecordImageByTravelId(travelId)

    fun loadPlaceAndScheduleByTravelId(travelId: Int) =
        recordImageDao.loadDistinctPlaceAndScheduleByTravelId(travelId)

    fun loadOneDataByTravelId(travelId: Int) =
        recordImageDao.loadFirstRowByTravelId(travelId, 1, 0)

    fun loadMainImagesByTravelId(travelId: Int) =
        newRecordImageDao.loadAnyImageWithDistinctPlaceByTravelId(travelId)

    fun updateCommentByImageId(comment: String, id: Int) =
        newRecordImageDao.updateRecordImageCommentById(comment, id)

    fun insertEachNewRecordImages(image: NewRecordImage) {
        coroutineScope.launch { newRecordImageDao.insert(image) }
    }
    fun insertNewRecordImages(images: List<NewRecordImage>) {
        coroutineScope.launch { newRecordImageDao.insert(*images.toTypedArray()) }
    }
    fun insertRecordImages(images: List<RecordImage>) {
        coroutineScope.launch { recordImageDao.insert(*images.toTypedArray()) }
    }
    fun deleteNewRecordImages(images: List<NewRecordImage>) {
        coroutineScope.launch { newRecordImageDao.delete(*images.toTypedArray()) }
    }

    fun deleteRecordImage(id: Int) {
        coroutineScope.launch {
            val data = recordImageDao.loadRecordImageById(id)
            if (data != null) {
                recordImageDao.delete(data)
            }
        }
    }
    fun deleteNewRecordImage(id: Int) {
        coroutineScope.launch {
            val data = newRecordImageDao.loadRecordImageById(id)
            if (data != null) {
                newRecordImageDao.delete(data)
            }
        }
    }
}
