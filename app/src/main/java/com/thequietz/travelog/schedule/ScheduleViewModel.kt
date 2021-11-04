package com.thequietz.travelog.schedule

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thequietz.travelog.data.ScheduleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel @Inject internal constructor(
    private val repository: ScheduleRepository
) : ViewModel() {
    val schedules: MediatorLiveData<List<Schedule>> = MediatorLiveData()

    init {
        schedules.value = null
        viewModelScope.launch {
            schedules.addSource(repository.loadSchedules()) { schedules.value = it }
        }
    }

    fun createSchedule() {
        // 임시 스케줄 생성
        repository.createSchedules(
            Schedule(
                name = "Sample",
                place = listOf("Place 1", "Place 2"),
                date = "2021.01.01 ~ 2021.01.02"
            )
        )
    }

    fun deleteSchedule(id: Int) {
        repository.deleteSchedule(id)
    }
}
