package com.thequietz.travelog.schedule.repository

import android.util.Log
import com.thequietz.travelog.data.db.dao.ScheduleDao
import com.thequietz.travelog.data.db.dao.ScheduleDetailDao
import com.thequietz.travelog.place.model.PlaceDetailModel
import com.thequietz.travelog.schedule.model.ScheduleDetailModel
import com.thequietz.travelog.schedule.model.ScheduleModel
import com.thequietz.travelog.schedule.model.SchedulePlaceModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ScheduleRepository @Inject constructor(
    private val scheduleDao: ScheduleDao,
    private val scheduleDetailDao: ScheduleDetailDao,
    private val coroutineScope: CoroutineScope
) {
    fun loadSchedules() = scheduleDao.loadAllSchedules()

    fun createSchedules(schedule: ScheduleModel, onInsert: (Int) -> (Unit)) {
        coroutineScope.launch {
            val result = scheduleDao.insert(schedule)
            onInsert(result[0].toInt())
        }
    }

    fun deleteSchedule(id: Int) {
        coroutineScope.launch {
            val data = scheduleDao.loadScheduleById(id)
            Log.d("Loaded Data", data[0].name)

            if (!data.isNullOrEmpty())
                scheduleDao.delete(data[0])
        }
    }

    fun loadScheduleDetailsByScheduleId(scheduleId: Int) =
        scheduleDetailDao.loadScheduleDetailsByScheduleId(scheduleId)

    fun loadScheduleByScheduleName(scheduleName: String) =
        scheduleDao.loadScheduleByName(scheduleName)

    fun createScheduleDetail(
        scheduleId: Int,
        scheduleOrder: Int,
        place: SchedulePlaceModel,
        date: String,
        destination: PlaceDetailModel
    ) {
        coroutineScope.launch {
            val newDetailModel = ScheduleDetailModel(
                scheduleId = scheduleId,
                scheduleOrder = scheduleOrder,
                place = place,
                date = date,
                destination = destination
            )

            scheduleDetailDao.insert(newDetailModel)
        }
    }
}
