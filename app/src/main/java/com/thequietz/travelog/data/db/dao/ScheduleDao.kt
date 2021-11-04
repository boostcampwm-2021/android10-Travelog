package com.thequietz.travelog.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.thequietz.travelog.schedule.Schedule

@Dao
abstract class ScheduleDao : BaseDao<Schedule> {
    @Query("SELECT * FROM Schedule")
    abstract fun loadAllSchedules(): LiveData<List<Schedule>>

    @Query("SELECT * FROM Schedule WHERE id =:id")
    abstract fun loadScheduleById(id: Int): List<Schedule>
}
