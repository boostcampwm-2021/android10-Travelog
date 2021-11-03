package com.thequietz.travelog.data

import android.content.Context
import com.thequietz.travelog.data.db.dao.ScheduleDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Singleton
    @Provides
    fun providesRepository(@ApplicationContext context: Context): Repository {
        return RepositoryImpl()
    }
}

interface Repository

class RepositoryImpl : Repository

@Singleton
class ScheduleRepository @Inject constructor(private val scheduleDao: ScheduleDao) {
    fun getSchedules() = scheduleDao.getAllSchedules()
}
