package com.thequietz.travelog.schedule.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.thequietz.travelog.data.ScheduleRepository
import com.thequietz.travelog.schedule.data.ColorRGB
import com.thequietz.travelog.schedule.data.ScheduleDetailItem
import com.thequietz.travelog.util.dateFormat
import com.thequietz.travelog.util.dateToString
import com.thequietz.travelog.util.stringToDate
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.Calendar
import java.util.Collections
import javax.inject.Inject

@HiltViewModel
class ScheduleDetailViewModel @Inject internal constructor(
    private val repository: ScheduleRepository,
) : ViewModel() {

    val item = mutableListOf<ScheduleDetailItem>()
    private val _itemList = MutableLiveData<List<ScheduleDetailItem>>()
    val itemList: LiveData<List<ScheduleDetailItem>> = _itemList

    private val indexList = mutableListOf<Int>()
    private var id = 0

    init {
        _itemList.value = item
    }

    fun initItemList(startDate: String, endDate: String) {
        if (item.isNotEmpty()) return

        var date = startDate
        var day = 1

        item.add(ScheduleDetailItem(id++, 1, getRandomColor(), date, day++))
        indexList.add(0)

        _itemList.value = item
        while (date != endDate) {
            date = addOneDate(date).toString()
            item.add(ScheduleDetailItem(id++, 1, getRandomColor(), date, day++))
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
        item.add(position, ScheduleDetailItem(id++, 2, color, name, index))
        _itemList.value = item
        indexList[index]++
    }

    fun deleteSchedule(id: Int) {
        val target = item.filter { it.id == id }[0]
        target.index?.let { indexList[it]-- }
        item.remove(target)
        _itemList.value = item
    }

    fun itemMove(fromPosition: Int, toPosition: Int) {
        val fromIndex = getDayIndex(fromPosition)
        indexList[fromIndex]--
        val toIndex = getDayIndex(toPosition)
        indexList[toIndex]++
        Collections.swap(item, fromPosition, toPosition)
    }

    private fun getDayIndex(position: Int): Int {
        var temp = 0
        var index = -1
        while (temp < position) {
            index++
            temp += indexList[index] + 1
        }
        return index
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
