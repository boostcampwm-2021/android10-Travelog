package com.thequietz.travelog.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.thequietz.travelog.schedule.model.ScheduleModel

@Dao
abstract class ScheduleDao : BaseDao<ScheduleModel> {
    @Query("SELECT * FROM Schedule")
    abstract fun getAllSchedules(): LiveData<List<ScheduleModel>>
}
