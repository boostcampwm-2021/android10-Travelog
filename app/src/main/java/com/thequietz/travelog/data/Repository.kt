package com.thequietz.travelog.data

import android.util.Log
import com.thequietz.travelog.api.PlaceRecommend
import com.thequietz.travelog.api.PlaceService
import com.thequietz.travelog.data.db.dao.RecordImageDao
import com.thequietz.travelog.data.db.dao.ScheduleDao
import com.thequietz.travelog.getTodayDate
import com.thequietz.travelog.guide.Place
import com.thequietz.travelog.guide.RecommendPlace
import com.thequietz.travelog.record.model.RecordImage
import com.thequietz.travelog.schedule.model.ScheduleModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GuideRepository @Inject constructor(
    private val placeService: PlaceService,
    private val placeRecommend: PlaceRecommend
) {

    suspend fun loadAllPlaceData(): List<Place> {
        val result = placeService.requestAll()
        return result.data
    }

    suspend fun loadAllDoSi(): List<Place> {
        val result = placeService.requestALLDoSi()
        return result.data
    }

    suspend fun loadDoSiByCode(code: String = "3"): List<Place> {
        val result = placeService.requestDoSiByCode(code)
        return result.data
    }

    suspend fun loadDoSiByKeyword(keyword: String = "서"): List<Place> {
        val result = placeService.requestAllByKeyword(keyword)
        return result.data
    }

    suspend fun loadRecommendPlaceData(areaCode: String = "1", sigunguCode: String = "10"): List<RecommendPlace> {
        val res = placeRecommend.requestRecommendPlace(areaCode, sigunguCode).response.body.items.item
        return res
    }

    suspend fun loadAreaData(areaCode: String = "1", requestType: String = "A01", pageNo: Int): List<RecommendPlace> {
        return try {
            val res = placeRecommend.requestAreaBased(areaCode, requestType, pageNo)
            val maxPage = (res.response.body.totalCnt / 10) + 1
            if (maxPage < pageNo) {
                return listOf()
            } else {
                return res.response.body.items.item
            }
        } catch (e: Exception) {
            listOf()
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
            val res = placeRecommend.requestFestival(getTodayDate(), areaCode, pageNo)
            val maxPage = (res.response.body.totalCnt / 10) + 1
            if (maxPage < pageNo) {
                return listOf()
            } else {
                return res.response.body.items.item
            }
        } catch (e: Exception) {
            listOf()
        }
    }
}

@Singleton
class RecordRepository @Inject constructor(
    private val recordImageDao: RecordImageDao,
    private val coroutineScope: CoroutineScope
) {
    fun loadRecordImages() = recordImageDao.loadAllRecordImages()

    fun createRecordImage(recordImage: RecordImage) {
        coroutineScope.launch { recordImageDao.insert(recordImage) }
    }

    fun updateRecordImageComment(comment: String, id: Int) {
        coroutineScope.launch { recordImageDao.updateRecordImageCommentById(comment, id) }
    }
    fun deleteRecordImage(id: Int) {
        coroutineScope.launch {
            val data = recordImageDao.loadRecordImageById(id)
            recordImageDao.delete(data)
        }
    }
}

@Singleton
class ScheduleRepository @Inject constructor(
    private val scheduleDao: ScheduleDao,
    private val coroutineScope: CoroutineScope
) {
    fun loadSchedules() = scheduleDao.loadAllSchedules()

    fun createSchedules(schedule: ScheduleModel) {
        coroutineScope.launch { scheduleDao.insert(schedule) }
    }

    fun deleteSchedule(id: Int) {
        coroutineScope.launch {
            val data = scheduleDao.loadScheduleById(id)
            Log.d("Loaded Data", data[0].name)

            if (!data.isNullOrEmpty())
                scheduleDao.delete(data[0])
        }
    }
}
