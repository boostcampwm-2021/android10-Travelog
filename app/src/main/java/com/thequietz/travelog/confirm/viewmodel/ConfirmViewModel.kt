package com.thequietz.travelog.confirm.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.thequietz.travelog.schedule.model.ScheduleDetailModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ConfirmViewModel @Inject constructor() : ViewModel() {
    private var _schedules = MutableLiveData<Map<String, List<ScheduleDetailModel>>>()
    val schedules: LiveData<Map<String, List<ScheduleDetailModel>>> get() = _schedules

    private var _currentSchedule = MutableLiveData<List<ScheduleDetailModel>>()
    val currentSchedule: LiveData<List<ScheduleDetailModel>> get() = _currentSchedule

    fun getSchedulesByNavArgs(schedules: Array<ScheduleDetailModel>) {
        val dataSet = schedules.map { it.date }.toSet()
        val newDataMap = mutableMapOf<String, List<ScheduleDetailModel>>()

        dataSet.forEachIndexed { idx, it ->
            newDataMap["Day ${idx + 1}"] = schedules.filter { schedule -> schedule.date == it }.toList()
        }

        _schedules.value = newDataMap
    }

    fun updateSchedule(key: String) {
        _currentSchedule.value = schedules.value?.get(key) ?: listOf()
    }
}
