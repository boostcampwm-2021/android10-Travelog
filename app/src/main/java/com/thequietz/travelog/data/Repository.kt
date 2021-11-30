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
import com.thequietz.travelog.getTodayDate
import com.thequietz.travelog.guide.Place
import com.thequietz.travelog.guide.RecommendPlace
import com.thequietz.travelog.record.model.RecordImage
import com.thequietz.travelog.util.festivalContentTypeId
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
                println("from server..")
                allRes
            } catch (e: JsonSyntaxException) {
                emptyList
            } catch (t: Throwable) {
                Log.d(TAG, t.stackTraceToString())
                emptyList
            }
        } else {
            println("from cache..")
            loadData
        }
    }

    suspend fun loadAllDoSi(): List<Place> {
        val loadData = placeDao.loadAllDOSI()
        return if (loadData.isEmpty()) {
            try {
                val res = placeService.requestALLDoSi().data
                println("from server..")
                res
            } catch (e: JsonSyntaxException) {
                emptyList
            } catch (t: Throwable) {
                Log.d(TAG, t.stackTraceToString())
                emptyList
            }
        } else {
            println("from cache..")
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
                println("from server..")
                res
            } catch (e: JsonSyntaxException) {
                emptyList
            } catch (t: Throwable) {
                Log.d(TAG, t.stackTraceToString())
                emptyList
            }
        } else {
            println("from cache..")
            loadData
        }
    }

    suspend fun loadDoSiByKeyword(keyword: String = "서"): List<Place> {
        val loadData = placeDao.loadAllPlaceByKeyWord("str%")
        return if (loadData.isEmpty()) {
            try {
                val res = placeService.requestAllByKeyword(keyword).data
                println("from server..")
                res
            } catch (e: JsonSyntaxException) {
                emptyList
            } catch (t: Throwable) {
                Log.d(TAG, t.stackTraceToString())
                emptyList
            }
        } else {
            println("from cache..")
            loadData
        }
    }

    suspend fun loadRecommendPlaceData(
        areaCode: String = "1",
        sigunguCode: String = "10"
    ): List<RecommendPlace> {
        val loadData =
            recommendPlaceDao.loadRecommendPlaceByAreaCodeAndSigunguCode(areaCode, sigunguCode)
        return if (loadData.isEmpty()) {
            try {
                val res = guideRecommendService.requestRecommendPlace(
                    areaCode,
                    sigunguCode,
                    NEW_TOUR_API_KEY
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
                println("from server..")
                res
            } catch (e: JsonSyntaxException) {
                emptyRecommendList
            } catch (t: Throwable) {
                Log.d(TAG, t.stackTraceToString())
                emptyRecommendList
            }
        } else {
            println("from cache..")
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
                    println("from server..")
                    resList
                }
            } catch (e: JsonSyntaxException) {
                return emptyRecommendList
            } catch (t: Throwable) {
                Log.d(TAG, t.stackTraceToString())
                return emptyRecommendList
            }
        } else {
            println("from cache..")
            return loadData
        }
    }

    suspend fun loadFestivalData(areaCode: String, pageNo: Int): List<RecommendPlace> {
        val loadData =
            recommendPlaceDao.loadRecommendFestivalByAreaCode(areaCode, festivalContentTypeId)
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
                    coroutineScope.launch {
                        resList.forEach {
                            if (it.eventStartDate == null) {
                                recommendPlaceDao.insert(
                                    it.copy(
                                        contentTypeId = festivalContentTypeId,
                                        eventStartDate = ""
                                    )
                                )
                            } else {
                                recommendPlaceDao.insert(it)
                            }
                        }
                    }
                    println("from server..")
                    resList
                }
            } catch (e: JsonSyntaxException) {
                return emptyRecommendList
            } catch (t: Throwable) {
                Log.d(TAG, t.stackTraceToString())
                return emptyRecommendList
            }
        } else {
            println("from cache..")
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

    fun loadAllIncludeDefault(travelId: Int) =
        joinRecordDao.loadJoinedRecordByTravelIdIncludeDefault(travelId)

    fun loadAll(travelId: Int) =
        joinRecordDao.loadJoinedRecordByTravelId(travelId)

    fun loadNewRecordImagesByTravelId(travelId: Int) =
        newRecordImageDao.loadNewRecordImagesByTravelId(travelId)

    fun loadRecordImagesByTravelId(travelId: Int) =
        recordImageDao.loadRecordImageByTravelId(travelId)

    fun loadPlaceAndScheduleByTravelId(travelId: Int) =
        recordImageDao.loadDistinctPlaceAndScheduleByTravelId(travelId)

    fun loadOneDataByTravelId(travelId: Int) =
        recordImageDao.loadFirstRowByTravelId(travelId, 1, 0)

    fun loadMainImagesByTravelId(travelId: Int) =
        newRecordImageDao.loadAnyImageWithDistinctPlaceByTravelId(travelId)

    fun insertNewRecordImages(images: List<NewRecordImage>) {
        coroutineScope.launch { newRecordImageDao.insert(*images.toTypedArray()) }
    }
    fun insertRecordImages(images: List<RecordImage>) {
        coroutineScope.launch { recordImageDao.insert(*images.toTypedArray()) }
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
