package com.thequietz.travelog.ui.schedule.viewmodel

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thequietz.travelog.ui.schedule.model.ScheduleModel
import com.thequietz.travelog.ui.schedule.repository.ScheduleRepository
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
            schedules.addSource(repository.loadAllSchedules()) { schedules.value = it }
        }
    }

    fun deleteSchedule(id: Int) {
        repository.deleteSchedule(id)
        repository.deleteSchedulesByScheduleId(id)
    }
}
