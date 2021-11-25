package com.thequietz.travelog.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.thequietz.travelog.data.db.AppDatabase
import com.thequietz.travelog.data.db.dao.PlaceDao
import com.thequietz.travelog.data.db.dao.RecommendPlaceDao
import com.thequietz.travelog.data.db.dao.RecordImageDao
import com.thequietz.travelog.data.db.dao.ScheduleDao
import com.thequietz.travelog.data.db.dao.ScheduleDetailDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "Travelog"
        ).addCallback(
            object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    CoroutineScope(Dispatchers.IO).launch {
                        /*provideScheduleDao(provideDatabase(appContext))
                            .insert(*SAMPLE_SCHEDULES.toTypedArray())*/
                        /*Log.d("InitDB", "OK")
                        provideRecordImageDao(provideDatabase(appContext))
                            .insert(*SAMPLE_RECORD_IMAGES.toTypedArray())*/
                    }
                }
            }
        ).build()
    }

    @Provides
    fun provideScheduleDao(appDatabase: AppDatabase): ScheduleDao {
        return appDatabase.scheduleDao()
    }

    @Provides
    fun provideScheduleDetailDao(appDatabase: AppDatabase): ScheduleDetailDao {
        return appDatabase.scheduleDetailDao()
    }

    @Provides
    fun provideRecordImageDao(appDatabase: AppDatabase): RecordImageDao {
        return appDatabase.recordImageDao()
    }

    @Provides
    fun providePlaceDao(appDatabase: AppDatabase): PlaceDao {
        return appDatabase.placeDao()
    }

    @Provides
    fun provideRecommendPlaceDao(appDatabase: AppDatabase): RecommendPlaceDao {
        return appDatabase.recommendPlaceDao()
    }

    @Provides
    fun provideCoroutineScope() = CoroutineScope(SupervisorJob())
}
