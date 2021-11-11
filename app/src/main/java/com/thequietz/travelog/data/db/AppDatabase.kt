package com.thequietz.travelog.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.thequietz.travelog.data.db.dao.RecordImageDao
import com.thequietz.travelog.data.db.dao.ScheduleDao
import com.thequietz.travelog.record.model.RecordImage
import com.thequietz.travelog.schedule.model.ScheduleModel

@Database(entities = [ScheduleModel::class, RecordImage::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun scheduleDao(): ScheduleDao
    abstract fun recordImageDao(): RecordImageDao
}
