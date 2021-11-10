package com.thequietz.travelog.schedule.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.thequietz.travelog.data.ScheduleRepository
import com.thequietz.travelog.schedule.data.Schedule
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ScheduleDetailViewModel @Inject internal constructor(
    private val repository: ScheduleRepository,
) : ViewModel() {

    private val _scheduleList = MutableLiveData<MutableList<Schedule>>()
    val scheduleList: LiveData<MutableList<Schedule>> = _scheduleList

}
