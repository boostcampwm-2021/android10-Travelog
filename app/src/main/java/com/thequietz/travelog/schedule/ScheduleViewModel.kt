package com.thequietz.travelog.schedule

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thequietz.travelog.data.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {
    private val _schedules = MutableLiveData<List<Schedule>>()
    val schedules: LiveData<List<Schedule>> = _schedules

    init {
        viewModelScope.launch {
            _schedules.value = listOf(
                Schedule(0, "경주 여행", listOf("경주"), "2021.08.08 ~ 2021.08.10"),
                Schedule(0, "서울 여행", listOf("서울, 경기"), "2021.10.05 ~ 2021.10.20"),
                Schedule(0, "강릉 여행", listOf("강릉"), "2021.12.24 ~ 2021.12.25"),
            )
        }
    }
}
