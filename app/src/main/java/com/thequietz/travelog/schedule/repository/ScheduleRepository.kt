package com.thequietz.travelog.schedule.repository

import android.util.Log
import com.thequietz.travelog.data.db.dao.ScheduleDao
import com.thequietz.travelog.data.db.dao.ScheduleDetailDao
import com.thequietz.travelog.schedule.model.ScheduleDetailModel
import com.thequietz.travelog.schedule.model.ScheduleModel
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
    fun loadAllSchedules() = scheduleDao.loadAllSchedules()

    fun loadScheduleById(id: Int) = scheduleDao.loadScheduleById(id)

    fun createSchedules(schedule: ScheduleModel, onInsert: (Int) -> (Unit)) {
        coroutineScope.launch {
            val result = scheduleDao.insert(schedule)
            onInsert(result.last().toInt())
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
        scheduleDetails: List<ScheduleDetailModel>
    ) {
        coroutineScope.launch {
            if (scheduleDetails.isNotEmpty()) {
                scheduleDetailDao.deleteScheduleDetailsByScheduleId(scheduleDetails[0].scheduleId)
                scheduleDetailDao.insert(*scheduleDetails.map { it.copy(id = 0) }.toTypedArray())
            }
        }
    }

    fun deleteSchedulesByScheduleId(id: Int) {
        coroutineScope.launch {
            scheduleDetailDao.deleteScheduleDetailsByScheduleId(id)
        }
    }

    fun loadScheduleDateList() = scheduleDao.loadAllDates()
}
