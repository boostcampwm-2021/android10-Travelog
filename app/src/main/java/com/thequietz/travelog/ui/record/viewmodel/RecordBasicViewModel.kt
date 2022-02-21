package com.thequietz.travelog.ui.record.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.thequietz.travelog.data.db.dao.NewRecordImage
import com.thequietz.travelog.ui.record.model.RecordBasic
import com.thequietz.travelog.ui.record.model.RecordBasicItem
import com.thequietz.travelog.ui.record.model.RecordImage
import com.thequietz.travelog.ui.record.repository.RecordBasicRepository
import com.thequietz.travelog.ui.schedule.model.ScheduleDetailModel
import com.thequietz.travelog.util.createDateFromDay
import com.thequietz.travelog.util.createDayFromDate
import com.thequietz.travelog.util.nextDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class RecordBasicViewModel @Inject constructor(
    private val repository: RecordBasicRepository
) : ViewModel() {
    private val _isEmpty = MutableLiveData<Boolean>()
    val isEmpty: LiveData<Boolean> = _isEmpty

    private val _title = MutableLiveData<String>()
    val title: LiveData<String> = _title

    private val _date = MutableLiveData<String>()
    val date: LiveData<String> = _date

    private val _headerDay = MutableLiveData("Day1")
    val headerDay: LiveData<String> = _headerDay

    private val _headerDate = MutableLiveData<String>()
    val headerDate: LiveData<String> = _headerDate

    private val _recordBasicItemList = MutableLiveData<List<RecordBasicItem>>()
    val recordBasicItemList: LiveData<List<RecordBasicItem>> = _recordBasicItemList

    fun loadData(travelId: Int, title: String, startDate: String, endDate: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val oldRecordImages = repository.loadRecordImagesByTravelId(travelId)
            val scheduleDetails =
                repository.loadScheduleDetailOrderByScheduleIdAndDate(travelId)

            if (oldRecordImages.isEmpty() && scheduleDetails.isEmpty()) {
                withContext(Dispatchers.Main) {
                    _isEmpty.value = true
                }
                return@launch
            }

            val newRecordImages = createRecordImages(scheduleDetails, title, startDate, endDate)
            val isSameOldAndNew = compareOldAndNewRecordImages(oldRecordImages, newRecordImages)

            if (!isSameOldAndNew) {
                val tempNewRecordImages = createNewRecordImages(newRecordImages)
                repository.deleteAndInsertNewRecordImages(travelId, true, tempNewRecordImages)
                repository.deleteAndInsertRecordImages(travelId, newRecordImages)
            }

            val recordBasic = createRecordBasicFromRecordImages(newRecordImages)

            withContext(Dispatchers.Main) {
                _title.value = recordBasic.title
                _date.value = "${recordBasic.startDate} ~ ${recordBasic.endDate}"
                _recordBasicItemList.value =
                    createListOfRecyclerViewAdapterItem(recordBasic)
            }
        }
    }

    private fun createRecordImages(
        scheduleDetails: List<ScheduleDetailModel>,
        title: String,
        startDate: String,
        endDate: String
    ): List<RecordImage> {
        val recordImages = mutableListOf<RecordImage>()

        for (scheduleDetail in scheduleDetails) {
            recordImages.add(
                RecordImage(
                    travelId = scheduleDetail.scheduleId,
                    title = title,
                    startDate = startDate,
                    endDate = endDate,
                    place = scheduleDetail.destination.name,
                    day = createDayFromDate(startDate, scheduleDetail.date),
                    lat = scheduleDetail.destination.geometry.location.latitude,
                    lng = scheduleDetail.destination.geometry.location.longitude
                )
            )
        }

        return recordImages.toList()
    }

    private fun compareOldAndNewRecordImages(
        oldRecordImages: List<RecordImage>,
        newRecordImages: List<RecordImage>
    ): Boolean {
        if (oldRecordImages.size != newRecordImages.size) return false

        for (i in oldRecordImages.indices) {
            if (oldRecordImages[i].travelId != newRecordImages[i].travelId ||
                oldRecordImages[i].title != newRecordImages[i].title ||
                oldRecordImages[i].startDate != newRecordImages[i].startDate ||
                oldRecordImages[i].endDate != newRecordImages[i].endDate ||
                oldRecordImages[i].day != newRecordImages[i].day ||
                oldRecordImages[i].place != newRecordImages[i].place ||
                oldRecordImages[i].lat != newRecordImages[i].lat ||
                oldRecordImages[i].lng != newRecordImages[i].lng
            ) {
                return false
            }
        }

        return true
    }

    private fun createNewRecordImages(newRecordImages: List<RecordImage>): List<NewRecordImage> {
        val tempNewRecordImages = mutableListOf<NewRecordImage>()

        newRecordImages.forEach { recordImage ->
            tempNewRecordImages.add(
                NewRecordImage().copy(
                    newTravelId = recordImage.travelId,
                    newTitle = recordImage.title,
                    newPlace = recordImage.place,
                    url = "empty",
                    comment = "코멘트를 남겨주세요!",
                    isDefault = true
                )
            )
        }

        return tempNewRecordImages.toList()
    }

    private fun createRecordBasicFromRecordImages(recordImages: List<RecordImage>): RecordBasic {
        val recordDestinationList = mutableListOf<RecordBasicItem.TravelDestination>()

        for (recordImage in recordImages) {
            if (recordDestinationList.isEmpty() ||
                recordImage.place != recordDestinationList.last().name
            ) {
                recordDestinationList.add(
                    RecordBasicItem.TravelDestination(
                        name = recordImage.place,
                        date = createDateFromDay(recordImage.startDate, recordImage.day),
                        day = recordImage.day,
                        lat = recordImage.lat,
                        lng = recordImage.lng
                    )
                )
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
        val lastDate = recordBasic.endDate.nextDate()
        var tempIndex = 0

        while (currDate != lastDate) {
            var seq = 1
            list.add(RecordBasicItem.RecordBasicHeader("Day$currDay", currDate))

            for (i in tempIndex until travelDestinations.size) {
                if (travelDestinations[i].date == currDate) {
                    travelDestinations[i].seq = seq++
                    list.add(travelDestinations[i])
                    tempIndex = i + 1
                }
            }

            currDay++
            currDate = currDate.nextDate()
        }

        return list.toList()
    }

    fun updateTargetList(day: String, date: String, targetList: MutableLiveData<MutableList<LatLng>>) {
        val tempRecordBasicItemList = _recordBasicItemList.value ?: return
        val list = mutableListOf<LatLng>()
        var isCurrentDay = false
        _headerDay.value = day
        _headerDate.value = date

        for (tempRecordBasicItem in tempRecordBasicItemList) {
            when (tempRecordBasicItem) {
                is RecordBasicItem.RecordBasicHeader -> {
                    isCurrentDay = tempRecordBasicItem.day == headerDay.value
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
}
