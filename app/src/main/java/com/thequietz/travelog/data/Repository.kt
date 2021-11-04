package com.thequietz.travelog.data

import android.content.Context
import com.thequietz.travelog.data.db.dao.ScheduleDao
import com.thequietz.travelog.schedule.Schedule
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
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
class ScheduleRepository @Inject constructor(
    private val scheduleDao: ScheduleDao,
    private val coroutineScope: CoroutineScope
) {
    fun loadSchedules() = scheduleDao.loadAllSchedules()

    fun createSchedules(schedule: Schedule) {
        coroutineScope.launch { scheduleDao.insert(schedule) }
    }

    fun deleteSchedule(id: Int) {
        coroutineScope.launch {
            val data = scheduleDao.loadScheduleById(id)

            if (!data.isNullOrEmpty())
                scheduleDao.delete(data[0])
        }
    }
}
