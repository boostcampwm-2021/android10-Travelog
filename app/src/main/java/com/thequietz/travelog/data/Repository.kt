package com.thequietz.travelog.data

import android.content.Context
import android.util.Log
import com.thequietz.travelog.ApiFactory
import com.thequietz.travelog.data.db.dao.ScheduleDao
import com.thequietz.travelog.guide.Place
import com.thequietz.travelog.guide.RecommendPlace
import com.thequietz.travelog.schedule.model.ScheduleModel
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

interface Repository {
    suspend fun loadAllPlaceData(cityName: String = "서"): List<Place>
    suspend fun loadRecommendPlaceData(areaCode: String = "1", sigungucode: String = "10"): List<RecommendPlace>
}

class RepositoryImpl : Repository {
    private val TourApi = ApiFactory.createTourApi()
    private val NewApi = ApiFactory.createNewApi()

    override suspend fun loadAllPlaceData(cityName: String): List<Place> {
        val result = NewApi.requestAllPlace(cityName)
        return result.data
    }

    override suspend fun loadRecommendPlaceData(
        areaCode: String,
        sigunguCode: String
    ): List<RecommendPlace> {
        val res = TourApi.requestRecommendPlace(areaCode, sigunguCode).response.body.items.item
        return res
    }
}

@Singleton
class ScheduleRepository @Inject constructor(
    private val scheduleDao: ScheduleDao,
    private val coroutineScope: CoroutineScope
) {
    fun loadSchedules() = scheduleDao.loadAllSchedules()

    fun createSchedules(schedule: ScheduleModel) {
        coroutineScope.launch { scheduleDao.insert(schedule) }
    }

    fun deleteSchedule(id: Int) {
        coroutineScope.launch {
            val data = scheduleDao.loadScheduleById(id)
            Log.d("Loaded Data", data[0].name)

            if (!data.isNullOrEmpty())
                scheduleDao.delete(data[0])
        }
    }
}
