package com.thequietz.travelog.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.thequietz.travelog.data.db.dao.ScheduleDao
import com.thequietz.travelog.schedule.Schedule

@Database(entities = [Schedule::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun scheduleDao(): ScheduleDao
}
