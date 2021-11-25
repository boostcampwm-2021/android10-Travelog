package com.thequietz.travelog.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.thequietz.travelog.data.db.dao.PlaceDao
import com.thequietz.travelog.data.db.dao.RecommendPlaceDao
import com.thequietz.travelog.data.db.dao.RecordImageDao
import com.thequietz.travelog.data.db.dao.ScheduleDao
import com.thequietz.travelog.data.db.dao.ScheduleDetailDao
import com.thequietz.travelog.guide.Place
import com.thequietz.travelog.guide.RecommendPlace
import com.thequietz.travelog.record.model.RecordImage
import com.thequietz.travelog.schedule.model.ScheduleDetailModel
import com.thequietz.travelog.schedule.model.ScheduleModel

@Database(entities = [ScheduleModel::class, ScheduleDetailModel::class, RecordImage::class, Place::class, RecommendPlace::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun scheduleDao(): ScheduleDao
    abstract fun scheduleDetailDao(): ScheduleDetailDao
    abstract fun recordImageDao(): RecordImageDao
    abstract fun placeDao(): PlaceDao
    abstract fun recommendPlaceDao(): RecommendPlaceDao
}
