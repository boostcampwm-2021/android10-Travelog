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
    repository: ScheduleRepository
) : ViewModel() {
    val schedules: MediatorLiveData<List<Schedule>> = MediatorLiveData()

    init {
        schedules.value = null
        viewModelScope.launch {
            schedules.addSource(repository.getSchedules()) { schedules.value = it }
        }
    }
}
