package com.thequietz.travelog.data

import android.util.Log
import com.thequietz.travelog.ApiFactory
import com.thequietz.travelog.data.db.dao.RecordImageDao
import com.thequietz.travelog.data.db.dao.ScheduleDao
import com.thequietz.travelog.getTodayDate
import com.thequietz.travelog.guide.Place
import com.thequietz.travelog.guide.RecommendPlace
import com.thequietz.travelog.record.RecordImage
import com.thequietz.travelog.schedule.model.ScheduleModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RepositoryImpl @Inject constructor() {
    private val TourApi = ApiFactory.createTourApi()
    private val PlaceListApi = ApiFactory.createPlaceListApi()

    suspend fun loadAllPlaceData(): List<Place> {
        val result = PlaceListApi.requestAll()
        return result.data
    }

    suspend fun loadAllDoSi(): List<Place> {
        val result = PlaceListApi.requestALLDoSi()
        return result.data
    }

    suspend fun loadDoSiByCode(code: String = "3"): List<Place> {
        val result = PlaceListApi.requestDoSiByCode(code)
        return result.data
    }

    suspend fun loadDoSiByKeyword(keyword: String = "서"): List<Place> {
        val result = PlaceListApi.requestAllByKeyword(keyword)
        return result.data
    }

    suspend fun loadRecommendPlaceData(areaCode: String = "1", sigunguCode: String = "10"): List<RecommendPlace> {
        val res = TourApi.requestRecommendPlace(areaCode, sigunguCode).response.body.items.item
        return res
    }

    suspend fun loadVacationSpotData(areaCode: String): List<RecommendPlace> {
        val res = TourApi.requestVacationSpot(areaCode).response.body.items.item
        return res
    }

    suspend fun loadFoodData(areaCode: String): List<RecommendPlace> {
        val res = TourApi.requestFood(areaCode).response.body.items.item
        return res
    }

    suspend fun loadFestivalData(areaCode: String): List<RecommendPlace> {
        val res = TourApi.requestFestival(getTodayDate(), areaCode).response.body.items.item
        return res
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
