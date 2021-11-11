package com.thequietz.travelog.schedule.viewmodel

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thequietz.travelog.data.ScheduleRepository
import com.thequietz.travelog.schedule.model.PlaceModel
import com.thequietz.travelog.schedule.model.ScheduleModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel @Inject internal constructor(
    private val repository: ScheduleRepository
) : ViewModel() {
    val schedules: MediatorLiveData<List<ScheduleModel>> = MediatorLiveData()

    init {
        schedules.value = null
        viewModelScope.launch {
            schedules.addSource(repository.loadSchedules()) { schedules.value = it }
        }
    }

    fun createSchedule() {
        // 임시 스케줄 생성
        repository.createSchedules(
            ScheduleModel(
                name = "Sample",
                place = listOf(
                    PlaceModel(
                        thumbnail = "",
                        areaCode = 0,
                        mapX = 35.241615f,
                        mapY = 128.695587f,
                        "Place1"
                    ),
                    PlaceModel(
                        thumbnail = "",
                        areaCode = 0,
                        mapX = 35.241615f,
                        mapY = 128.695587f,
                        "Place2"
                    )
                ),
                date = "2021.01.01 ~ 2021.01.02"
            )
        )
    }

    fun deleteSchedule(id: Int) {
        repository.deleteSchedule(id)
    }
}
