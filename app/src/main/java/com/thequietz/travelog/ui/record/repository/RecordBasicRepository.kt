package com.thequietz.travelog.ui.record.repository

import com.thequietz.travelog.data.db.dao.NewRecordImage
import com.thequietz.travelog.data.db.dao.NewRecordImageDao
import com.thequietz.travelog.data.db.dao.RecordImageDao
import com.thequietz.travelog.data.db.dao.ScheduleDao
import com.thequietz.travelog.data.db.dao.ScheduleDetailDao
import com.thequietz.travelog.ui.record.model.RecordImage
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

    fun deleteRecordImageByPlace(place: String) {
        coroutineScope.launch {
            recordImageDao.deleteRecordImageByPlace(place)
        }
    }

    fun deleteAndInsertRecordImages(travelId: Int, recordImages: List<RecordImage>) {
        coroutineScope.launch {
            recordImageDao.deleteAndInsertRecordImages(travelId, recordImages)
        }
    }

    fun deleteAndInsertNewRecordImages(
        travelId: Int,
        isDefault: Boolean,
        tempNewRecordImages: List<NewRecordImage>
    ) {
        coroutineScope.launch {
            newRecordImageDao.deleteAndInsertNewRecordImages(
                travelId,
                isDefault,
                tempNewRecordImages
            )
        }
    }
}
