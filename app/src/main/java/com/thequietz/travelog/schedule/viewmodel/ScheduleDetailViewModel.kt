package com.thequietz.travelog.schedule.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thequietz.travelog.place.model.PlaceDetailModel
import com.thequietz.travelog.schedule.data.ColorRGB
import com.thequietz.travelog.schedule.data.ScheduleDetailItem
import com.thequietz.travelog.schedule.data.TYPE_CONTENT
import com.thequietz.travelog.schedule.data.TYPE_HEADER
import com.thequietz.travelog.schedule.model.ScheduleDetailModel
import com.thequietz.travelog.schedule.model.ScheduleModel
import com.thequietz.travelog.schedule.model.SchedulePlaceModel
import com.thequietz.travelog.schedule.repository.ScheduleRepository
import com.thequietz.travelog.util.dateToString
import com.thequietz.travelog.util.stringToDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Collections
import javax.inject.Inject

@HiltViewModel
class ScheduleDetailViewModel @Inject internal constructor(
    private val repository: ScheduleRepository
) : ViewModel() {
    var isInitial = true

    var selectedIndex = 0
    var selectedDate = ""

    private lateinit var schedule: ScheduleModel

    val item = mutableListOf<ScheduleDetailItem>()
    private val _itemList = MutableLiveData<MutableList<ScheduleDetailItem>>()
    val itemList: LiveData<MutableList<ScheduleDetailItem>> = _itemList
    private val _detailList = MutableLiveData<MutableList<ScheduleDetailModel>>()
    val detailList: LiveData<MutableList<ScheduleDetailModel>> = _detailList
    private val _colorList = MutableLiveData<MutableList<ColorRGB>>()
    val colorList: LiveData<MutableList<ColorRGB>> = _colorList

    private val _placeDetailList: MediatorLiveData<List<ScheduleDetailModel>> = MediatorLiveData()
    val placeDetailList: LiveData<List<ScheduleDetailModel>> = _placeDetailList

    private val indexList = mutableListOf<Int>()
    private var id = 0

    init {
        _itemList.value = item
        _detailList.value = mutableListOf()
        _colorList.value = mutableListOf()
    }

    fun initItemList(startDate: String, endDate: String) {
        if (item.isNotEmpty()) return

        var date = startDate
        var day = 1

        while (date <= endDate) {
            item.add(ScheduleDetailItem(id++, TYPE_HEADER, getRandomColor(), date, day++))
            date = addOneDate(date).toString()
            indexList.add(0)
            _itemList.value = item.toMutableList()
        }
    }

    fun initDetailList(startDate: String, endDate: String, detailList: List<ScheduleDetailModel>) {
        var date = startDate
        val tempIndex = selectedIndex
        selectedIndex = 0

        while (date <= endDate) {
            detailList.filter { it.date == date }.forEach {
                addScheduleDetail(it.destination, it.date)
            }
            date = addOneDate(date).toString()
            selectedIndex++
        }
        selectedIndex = tempIndex
    }

    fun loadSchedule(schedule: ScheduleModel) {
        this.schedule = schedule

        if (!isInitial)
            return

        viewModelScope.launch {
            _placeDetailList.addSource(repository.loadScheduleDetailsByScheduleId(schedule.id)) { list ->
                _placeDetailList.value = list
                initDetailList(
                    schedule.date.split("~")[0],
                    schedule.date.split("~")[1],
                    list
                )
                isInitial = false
            }
        }
    }

    fun createSchedule(name: String, schedulePlaces: List<SchedulePlaceModel>, date: String) {
        if (!isInitial)
            return

        repository.createSchedules(
            ScheduleModel(
                name = name,
                schedulePlace = schedulePlaces,
                date = date
            )
        ) {
            schedule = ScheduleModel(it, name, schedulePlaces, date)
            isInitial = false
        }
    }

    fun saveSchedule() {
        detailList.value?.let { repository.createScheduleDetail(it) }
    }

    fun addScheduleDetail(placeDetail: PlaceDetailModel, date: String = selectedDate) {
        var numPriors = 0
        for (i in 0..selectedIndex) {
            numPriors += indexList[i]
        }
        val position = selectedIndex + numPriors + 1

        val temp = detailList.value?.apply {
            add(
                numPriors,
                ScheduleDetailModel(
                    scheduleId = schedule.id,
                    place = schedule.schedulePlace[0],
                    date = date,
                    destination = placeDetail
                )
            )
        }?.toMutableList()
        _detailList.value = temp!!

        applyScheduleDetails(position, placeDetail, numPriors)
    }

    private fun applyScheduleDetails(position: Int, placeDetail: PlaceDetailModel, numPriors: Int) {
        val color = getRandomColor()
        _colorList.value = colorList.value?.toMutableList()?.apply { add(numPriors, color) }

        item.add(
            position,
            ScheduleDetailItem(id++, TYPE_CONTENT, color, placeDetail.name, selectedIndex)
        )
        _itemList.value = item.toMutableList()
        indexList[selectedIndex]++
    }

    fun deleteSchedule(position: Int) {
        val dayIndex = getDayIndex(position)
        indexList[dayIndex]--
        val detailIndex = position - (dayIndex + 1)

        item.removeAt(position)
        _itemList.value = item.toMutableList()

        _detailList.value = detailList.value?.toMutableList()?.apply {
            removeAt(detailIndex)
        }

        _colorList.value = colorList.value?.toMutableList()?.apply {
            removeAt(detailIndex)
        }
    }

    fun itemMove(fromPosition: Int, toPosition: Int) {
        val tempDetails = detailList.value
        val tempColors = colorList.value

        val fromIndex = getDayIndex(fromPosition)
        indexList[fromIndex]--
        val fromDetailIndex = fromPosition - (fromIndex + 1)

        val toIndex = getDayIndex(toPosition)
        indexList[toIndex]++
        val toDetailIndex = toPosition - (toIndex + 1)

        if (item[toPosition].type == TYPE_HEADER) {
            if (toPosition > fromPosition)
                tempDetails?.get(fromDetailIndex)?.apply {
                    addOneDate(this.date).let { if (it != null) date = it }
                }
            else if (toPosition < fromPosition)
                tempDetails?.get(fromDetailIndex)?.apply {
                    subOneDate(this.date).let { if (it != null) date = it }
                }
        }

        Collections.swap(item, fromPosition, toPosition)
        _itemList.value = item

        Collections.swap(tempDetails, fromDetailIndex, toDetailIndex)
        Collections.swap(tempColors, fromDetailIndex, toDetailIndex)
        tempDetails.let { _detailList.value = it }
        tempColors.let { _colorList.value = it }
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
        cal.apply {
            time = stringToDate(date)
            add(Calendar.DATE, 1)
        }
        return stringToDate(dateToString(cal.time))?.let { dateToString(it) }
    }

    private fun subOneDate(date: String): String? {
        val cal: Calendar = Calendar.getInstance()
        cal.apply {
            time = stringToDate(date)
            add(Calendar.DATE, -1)
        }
        return stringToDate(dateToString(cal.time))?.let { dateToString(it) }
    }
}
