package com.thequietz.travelog.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.thequietz.travelog.data.db.dao.JoinRecord
import com.thequietz.travelog.data.db.dao.JoinRecordDao
import com.thequietz.travelog.data.db.dao.NewRecordImage
import com.thequietz.travelog.data.db.dao.NewRecordImageDao
import com.thequietz.travelog.data.db.dao.PlaceDao
import com.thequietz.travelog.data.db.dao.RecommendPlaceDao
import com.thequietz.travelog.data.db.dao.RecordImageDao
import com.thequietz.travelog.data.db.dao.ScheduleDao
import com.thequietz.travelog.data.db.dao.ScheduleDetailDao
import com.thequietz.travelog.ui.guide.Place
import com.thequietz.travelog.ui.guide.RecommendPlace
import com.thequietz.travelog.ui.record.model.RecordImage
import com.thequietz.travelog.ui.schedule.model.ScheduleDetailModel
import com.thequietz.travelog.ui.schedule.model.ScheduleModel

@Database(entities = [ScheduleModel::class, ScheduleDetailModel::class, RecordImage::class, Place::class, RecommendPlace::class, NewRecordImage::class, JoinRecord::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun scheduleDao(): ScheduleDao
    abstract fun scheduleDetailDao(): ScheduleDetailDao
    abstract fun recordImageDao(): RecordImageDao
    abstract fun placeDao(): PlaceDao
    abstract fun recommendPlaceDao(): RecommendPlaceDao
    abstract fun newRecordImageDao(): NewRecordImageDao
    abstract fun joinRecordDao(): JoinRecordDao
}
