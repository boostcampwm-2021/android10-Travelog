package com.thequietz.travelog.schedule.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.thequietz.travelog.data.ScheduleRepository
import com.thequietz.travelog.schedule.data.ColorRGB
import com.thequietz.travelog.schedule.data.Schedule
import com.thequietz.travelog.schedule.data.ScheduleDetailItem
import com.thequietz.travelog.schedule.data.SchedulePlace
import com.thequietz.travelog.util.dateFormat
import com.thequietz.travelog.util.dateToString
import com.thequietz.travelog.util.stringToDate
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class ScheduleDetailViewModel @Inject internal constructor(
    private val repository: ScheduleRepository,
) : ViewModel() {

    private val _scheduleList = MutableLiveData<MutableList<Schedule>>()
    val scheduleList: LiveData<MutableList<Schedule>> = _scheduleList

    val item = mutableListOf<ScheduleDetailItem>()
    private val _itemList = MutableLiveData<List<ScheduleDetailItem>>()
    val itemList: LiveData<List<ScheduleDetailItem>> = _itemList

    /* private val _indexList = MutableLiveData<MutableList<Int>>()
     val indexList: LiveData<MutableList<Int>> = _indexList*/

    private val indexList = mutableListOf<Int>()

    init {
        _itemList.value = item
    }

    fun initItemList(startDate: String, endDate: String) {
        if (item.isNotEmpty()) return

        var date = startDate
        var day = 1

        item.add(ScheduleDetailItem(1, getRandomColor(), date, day++))
        indexList.add(0)

        _itemList.value = item
        while (date != endDate) {
            date = addOneDate(date).toString()
            item.add(ScheduleDetailItem(1, getRandomColor(), date, day++))
            indexList.add(0)
            _itemList.value = item
        }
    }

    fun addSchedule(index: Int, name: String) {
        val color = getRandomColor()
        var temp = 0
        for (i in 0..index) {
            temp += indexList[i]
        }
        val position = index + temp + 1
        item.add(position, ScheduleDetailItem(2, color, name, null))
        _itemList.value = item
        _scheduleList.value?.get(index)?.placeList?.add(SchedulePlace(name, color))
        indexList[index]++
    }

    private fun getRandomColor(): ColorRGB {
        return ColorRGB((0..255).random(), (0..255).random(), (0..255).random())
    }

    private fun addOneDate(date: String): String? {
        val cal: Calendar = Calendar.getInstance()
        cal.time = stringToDate(date)
        cal.add(Calendar.DATE, 1)
        val str = dateFormat.format(cal.time)
        return stringToDate(str)?.let { dateToString(it) }
    }
}
