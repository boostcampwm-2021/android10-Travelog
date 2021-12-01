package com.thequietz.travelog.confirm.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.thequietz.travelog.schedule.model.ScheduleDetailModel
import com.thequietz.travelog.schedule.model.ScheduleModel
import com.thequietz.travelog.util.addOneDate
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.SortedMap
import javax.inject.Inject

@HiltViewModel
class ConfirmViewModel @Inject constructor() : ViewModel() {
    private var _schedules = MutableLiveData<SortedMap<String, List<ScheduleDetailModel>>>()
    val schedules: LiveData<SortedMap<String, List<ScheduleDetailModel>>> get() = _schedules

    private var _currentSchedule = MutableLiveData<List<ScheduleDetailModel>>()
    val currentSchedule: LiveData<List<ScheduleDetailModel>> get() = _currentSchedule

    fun getSchedulesByNavArgs(
        schedule: ScheduleModel,
        scheduleDetails: Array<ScheduleDetailModel>
    ) {
        var nowDate = schedule.date.split("~")[0]
        val endDate = schedule.date.split("~")[1]

        val dateSet = mutableListOf<String>()
        while (nowDate != endDate) {
            dateSet.add(nowDate)
            nowDate = addOneDate(nowDate)
        }
        dateSet.add(endDate)

        val newDataMap = hashMapOf<String, List<ScheduleDetailModel>>()

        dateSet.forEachIndexed { idx, it ->
            newDataMap["Day ${String.format("%02d", idx + 1)}"] =
                scheduleDetails.filter { schedule -> schedule.date == it }.toList()
        }

        _schedules.value = newDataMap.toSortedMap()
    }

    fun updateSchedule(key: String) {
        _currentSchedule.value = schedules.value?.get(key) ?: listOf()
    }
}
