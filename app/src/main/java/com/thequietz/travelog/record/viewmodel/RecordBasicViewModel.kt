package com.thequietz.travelog.record.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.thequietz.travelog.record.model.RecordBasic
import com.thequietz.travelog.record.model.RecordBasicItem
import com.thequietz.travelog.record.model.RecordImage
import com.thequietz.travelog.record.repository.RecordBasicRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class RecordBasicViewModel @Inject constructor(
    private val repository: RecordBasicRepository
) : ViewModel() {
    private val _title = MutableLiveData<String>()
    val title: LiveData<String> = _title

    private val _date = MutableLiveData<String>()
    val date: LiveData<String> = _date

    private val _recordBasicItemList = MutableLiveData<List<RecordBasicItem>>()
    val recordBasicItemList: LiveData<List<RecordBasicItem>> = _recordBasicItemList

    private val _recordImageList = MutableLiveData<List<RecordImage>>()
    val recordImageList: LiveData<List<RecordImage>> = _recordImageList

    fun loadData(travelId: Int, title: String, startDate: String, endDate: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val recordImages = repository.loadRecordImagesByTravelId(travelId)

            if (recordImages.isEmpty()) {
                val scheduleDetails =
                    repository.loadScheduleDetailOrderByScheduleIdAndDate(travelId)
                val recordImages = mutableListOf<RecordImage>()

                for ((group, scheduleDetail) in scheduleDetails.withIndex()) {
                    recordImages.add(
                        RecordImage(
                            travelId = scheduleDetail.scheduleId,
                            title = title,
                            startDate = startDate,
                            endDate = endDate,
                            place = scheduleDetail.destination.name,
                            day = createDayFromDate(startDate, scheduleDetail.date),
                            group = group,
                            lat = scheduleDetail.destination.geometry.location.latitude,
                            lng = scheduleDetail.destination.geometry.location.longitude
                        )
                    )
                }

                withContext(Dispatchers.Main) {
                    _recordImageList.value = recordImages.toList()
                }

                repository.insertRecordImages(recordImages.toList())

                return@launch
            }

            withContext(Dispatchers.Main) {
                _recordImageList.value = recordImages
            }
        }
    }

    fun createData() {
        val recordImages = _recordImageList.value ?: emptyList()
        val recordBasic = createRecordBasicFromRecordImages(recordImages)
        _title.value = recordBasic.title
        _date.value = "${recordBasic.startDate} ~ ${recordBasic.endDate}"
        _recordBasicItemList.value =
            createListOfRecyclerViewAdapterItem(recordBasic)
    }

    private fun createDayFromDate(startDate: String, date: String): String {
        val tempStartDate = startDate.split('.').map { it.toInt() }
        val tempDate = date.split('.').map { it.toInt() }
        val isLeapYear = tempDate[0] % 4 == 0
        val dayOfMonth =
            if (isLeapYear) listOf(0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)
            else listOf(0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)

        // 2020.12.30 ~ 2021.1.4
        var day = 0
        if (tempStartDate[0] < tempDate[0]) {
            day = dayOfMonth[tempStartDate[1]] - tempStartDate[2]

            for (i in tempStartDate[1] + 1..12) {
                day += dayOfMonth[i]
            }

            day += tempDate[2]

            for (i in tempDate[1] - 1 downTo 1) {
                day += dayOfMonth[i]
            }
        }
        // 2021.1.4 ~ 2021.3.5
        else if (tempStartDate[1] < tempDate[1]) {
            day = dayOfMonth[tempStartDate[1]] - tempStartDate[2]

            for (i in tempStartDate[1] + 1 until tempDate[1]) {
                day += dayOfMonth[i]
            }

            day += tempDate[2]
        }
        // 2021.1.4 ~ 2021.1.5
        else {
            day = tempDate[2] - tempStartDate[2]
        }

        return "Day${day + 1}"
    }

    private fun createDateFromDay(startDate: String, day: String): String {
        val tempDate = startDate.split('.').map { it.toInt() }
        val tempDay = day.substring(3).toInt() - 1
        val isLeapYear = tempDate[0] % 4 == 0
        val dayOfMonth =
            if (isLeapYear) listOf(0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)
            else listOf(0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)

        // TODO("2달 이상 고려해야함.")

        if (tempDate[2] + tempDay <= dayOfMonth[tempDate[1]]) {
            return "${tempDate[0]}.${tempDate[1]}.${tempDate[2] + tempDay}"
        }

        if (tempDate[1] + 1 <= 12) {
            return "${tempDate[0]}.${tempDate[1] + 1}.${(tempDate[2] + tempDay) % dayOfMonth[tempDate[1]]}"
        }

        return "${tempDate[0] + 1}.1.${(tempDate[2] + tempDay) % dayOfMonth[tempDate[1]]}"
    }

    private fun createRecordBasicFromRecordImages(recordImages: List<RecordImage>): RecordBasic {
        val recordImageList = mutableListOf<String>()
        val recordDestinationList = mutableListOf<RecordBasicItem.TravelDestination>()

        for (i in recordImages.indices) {
            recordImageList.add(recordImages[i].url)

            if ((i + 1 < recordImages.size && recordImages[i].place != recordImages[i + 1].place) ||
                (i != 0 && i == recordImages.lastIndex && recordImages[i - 1].place != recordImages[i].place)
            ) {
                recordDestinationList.add(
                    RecordBasicItem.TravelDestination(
                        recordImages[i].place,
                        createDateFromDay(recordImages[i].startDate, recordImages[i].day),
                        recordImages[i].day,
                        recordImages[i].group,
                        recordImageList.toList(),
                        recordImages[i].lat,
                        recordImages[i].lng
                    )
                )
                recordImageList.clear()
            }
        }

        return RecordBasic(
            recordImages.first().travelId,
            recordImages.first().title,
            recordImages.first().startDate,
            recordImages.first().endDate,
            recordDestinationList.toList()
        )
    }

    private fun createListOfRecyclerViewAdapterItem(recordBasic: RecordBasic): List<RecordBasicItem> {
        val travelDestinations = recordBasic.travelDestinations
        val list = mutableListOf<RecordBasicItem>()

        var currDay = 1
        var currDate = recordBasic.startDate
        var tempIndex = 0

        while (currDate != recordBasic.endDate) {
            list.add(RecordBasicItem.RecordBasicHeader("Day$currDay", currDate))

            for (i in tempIndex until travelDestinations.size) {
                if (travelDestinations[i].date == currDate) {
                    list.add(travelDestinations[i])
                    tempIndex = i
                }
            }

            currDay++
            currDate = nextDate(currDate)
        }

        return list.toList()
    }

    private fun nextDate(currDate: String): String {
        val tempDate = currDate.split('.').map { it.toInt() }
        val isLeapYear = tempDate[0] % 4 == 0
        val dayOfMonth =
            if (isLeapYear) listOf(0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)
            else listOf(0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)

        if (tempDate[2] + 1 <= dayOfMonth[tempDate[1]]) {
            return "${tempDate[0]}.${tempDate[1]}.${tempDate[2] + 1}"
        }

        if (tempDate[1] + 1 <= 12) {
            return "${tempDate[0]}.${tempDate[1] + 1}.1"
        }

        return "${tempDate[0] + 1}.1.1"
    }

    fun addImage(uri: Uri, position: Int) {
        val tempRecordBasicItemList = _recordBasicItemList.value ?: return
        tempRecordBasicItemList.getOrNull(position) ?: return

        val tempRecordBasicItem =
            tempRecordBasicItemList[position] as RecordBasicItem.TravelDestination
        val tempImageList = tempRecordBasicItem.images.toMutableList()
        val imageUrl = uri.toString()

        if (tempImageList[0] == "") {
            tempImageList[0] = imageUrl
        } else {
            tempImageList.add(imageUrl)
        }

        val item = RecordBasicItem.TravelDestination(
            tempRecordBasicItem.name,
            tempRecordBasicItem.date,
            tempRecordBasicItem.day,
            tempRecordBasicItem.group,
            tempImageList.toList(),
            tempRecordBasicItem.lat,
            tempRecordBasicItem.lng
        )

        val tempRecordBasicItemMutableList = tempRecordBasicItemList.toMutableList()
        tempRecordBasicItemMutableList[position] = item
        _recordBasicItemList.value = tempRecordBasicItemMutableList.toList()

        viewModelScope.launch(Dispatchers.IO) {
            val tempRecordImageList = _recordImageList.value ?: return@launch
            val tempRecordImage =
                tempRecordImageList.find { it.place == item.name } ?: return@launch

            if (tempRecordImage.url == "") {
                repository.deleteRecordImageById(tempRecordImage.id)
            }

            repository.insertRecordImage(
                RecordImage(
                    travelId = tempRecordImage.travelId,
                    title = tempRecordImage.title,
                    startDate = tempRecordImage.startDate,
                    endDate = tempRecordImage.endDate,
                    day = tempRecordImage.day,
                    place = tempRecordImage.place,
                    url = imageUrl,
                    group = tempRecordImage.group,
                    lat = tempRecordImage.lat,
                    lng = tempRecordImage.lng
                )
            )
        }
    }

    fun deleteRecord(position: Int) {
        val tempRecordBasicItemList = _recordBasicItemList.value ?: return
        val tempRecordBasicItemMutableList = tempRecordBasicItemList.toMutableList()

        tempRecordBasicItemMutableList.getOrNull(position) ?: return
        val removedItem = tempRecordBasicItemMutableList.removeAt(position)

        _recordBasicItemList.value = tempRecordBasicItemMutableList.toList()

        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteRecordImageByPlace((removedItem as RecordBasicItem.TravelDestination).name)
        }
    }

    fun updateTargetList(day: String, targetList: MutableLiveData<MutableList<LatLng>>) {
        val tempRecordBasicItemList = _recordBasicItemList.value ?: return
        val list = mutableListOf<LatLng>()
        var isCurrentDay = false

        for (tempRecordBasicItem in tempRecordBasicItemList) {
            when (tempRecordBasicItem) {
                is RecordBasicItem.RecordBasicHeader -> {
                    isCurrentDay = tempRecordBasicItem.day == day
                }
                is RecordBasicItem.TravelDestination -> {
                    if (isCurrentDay) {
                        list.add(LatLng(tempRecordBasicItem.lat, tempRecordBasicItem.lng))
                    }
                }
            }
        }

        targetList.value = list
    }

    fun getIndexByGroupAndDay(group: Int, day: String): Int {
        val recordImageList = _recordImageList.value ?: return 0

        for ((index, recordImage) in recordImageList.withIndex()) {
            if (recordImage.group == group && recordImage.day == day) {
                return index
            }
        }

        return 0
    }
}
