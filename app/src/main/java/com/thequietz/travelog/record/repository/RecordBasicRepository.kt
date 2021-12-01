package com.thequietz.travelog.record.repository

import com.thequietz.travelog.data.db.dao.NewRecordImageDao
import com.thequietz.travelog.data.db.dao.RecordImageDao
import com.thequietz.travelog.data.db.dao.ScheduleDao
import com.thequietz.travelog.data.db.dao.ScheduleDetailDao
import com.thequietz.travelog.record.model.RecordImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecordBasicRepository @Inject constructor(
    private val scheduleDao: ScheduleDao,
    private val scheduleDetailDao: ScheduleDetailDao,
    private val recordImageDao: RecordImageDao,
    private val newRecordImageDao: NewRecordImageDao,
    private val coroutineScope: CoroutineScope
) {
    fun loadAllSchedule() = scheduleDao.loadAllSchedule()

    fun loadAllNewRecordImages() = newRecordImageDao.loadAllNewRecordImages()

    fun loadScheduleDetailOrderByScheduleIdAndDate(scheduleId: Int) =
        scheduleDetailDao.loadScheduleDetailsByScheduleIdOrderByScheduleIdAndDate(scheduleId)

    fun loadRecordImagesByTravelId(travelId: Int) =
        recordImageDao.loadRecordImageByTravelId(travelId)

    fun insertRecordImages(images: List<RecordImage>) {
        coroutineScope.launch { recordImageDao.insert(*images.toTypedArray()) }
    }

    fun deleteRecordImageById(id: Int) {
        coroutineScope.launch {
            recordImageDao.deleteRecordImageById(id)
        }
    }

    fun deleteRecordImageByPlace(place: String) {
        coroutineScope.launch {
            recordImageDao.deleteRecordImageByPlace(place)
        }
    }

    fun deleteRecordImageByTravelId(travelId: Int) {
        coroutineScope.launch {
            recordImageDao.deleteRecordImageByTravelId(travelId)
        }
    }

    fun deleteNewRecordImageByTravelIdAndIsDefault(travelId: Int, isDefault: Boolean) {
        coroutineScope.launch {
            newRecordImageDao.deleteNewRecordImageByTravelIdAndIsDefault(travelId, isDefault)
        }
    }
}
