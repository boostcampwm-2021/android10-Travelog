package com.thequietz.travelog.confirm.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.thequietz.travelog.schedule.model.ScheduleDetailModel
import com.thequietz.travelog.schedule.model.ScheduleModel
import com.thequietz.travelog.util.addOneDate
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ConfirmViewModel @Inject constructor() : ViewModel() {
    private var _schedules = MutableLiveData<Map<String, List<ScheduleDetailModel>>>()
    val schedules: LiveData<Map<String, List<ScheduleDetailModel>>> get() = _schedules

    private var _currentSchedule = MutableLiveData<List<ScheduleDetailModel>>()
    val currentSchedule: LiveData<List<ScheduleDetailModel>> get() = _currentSchedule

    fun getSchedulesByNavArgs(
        schedule: ScheduleModel,
        scheduleDetails: Array<ScheduleDetailModel>
    ) {
        var nowDate = schedule.date.split("~")[0]
        val endDate = schedule.date.split("~")[1]

        val dateSet = hashSetOf<String>().apply {
            while (nowDate != endDate) {
                add(nowDate)
                nowDate = addOneDate(nowDate)
            }
            add(endDate)
        }.sortedBy { it }
        val newDataMap = hashMapOf<String, List<ScheduleDetailModel>>()

        dateSet.forEachIndexed { idx, it ->
            newDataMap["Day ${idx + 1}"] =
                scheduleDetails.filter { schedule -> schedule.date == it }.toList()
        }
        Log.d("List-schedules", newDataMap.map { it.value.map { t -> t.id } }.toString())

        _schedules.value = newDataMap
    }

    fun updateSchedule(key: String) {
        _currentSchedule.value = schedules.value?.get(key) ?: listOf()
    }
}
