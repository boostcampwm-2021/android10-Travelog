package com.thequietz.travelog.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.thequietz.travelog.ui.schedule.model.ScheduleModel

@Dao
abstract class ScheduleDao : BaseDao<ScheduleModel> {
    @Query("SELECT * FROM Schedule")
    abstract fun loadAllSchedules(): LiveData<List<ScheduleModel>>

    @Query("SELECT * FROM Schedule")
    abstract fun loadAllSchedule(): List<ScheduleModel>

    @Query("SELECT * FROM Schedule WHERE id =:id")
    abstract fun loadScheduleById(id: Int): List<ScheduleModel>

    @Query("SELECT date FROM Schedule")
    abstract fun loadAllDates(): List<String>

    @Query("SELECT * FROM Schedule WHERE name =:name")
    abstract fun loadScheduleByName(name: String): List<ScheduleModel>
}
