package com.thequietz.travelog.record.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.thequietz.travelog.data.db.dao.NewRecordImage
import com.thequietz.travelog.record.model.RecordBasic
import com.thequietz.travelog.record.model.RecordBasicItem
import com.thequietz.travelog.record.model.RecordImage
import com.thequietz.travelog.record.repository.RecordBasicRepository
import com.thequietz.travelog.schedule.model.ScheduleDetailModel
import com.thequietz.travelog.util.createDateFromDay
import com.thequietz.travelog.util.createDayFromDate
import com.thequietz.travelog.util.nextDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
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

    private val _recordBasicItemList = MutableLiveData<List<RecordBasicItem>>()
    val recordBasicItemList: LiveData<List<RecordBasicItem>> = _recordBasicItemList

    private val _recordImageList = MutableLiveData<List<RecordImage>>()
    val recordImageList: LiveData<List<RecordImage>> = _recordImageList

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
                launch(Dispatchers.IO) {
                    repository.deleteNewRecordImageByTravelIdAndIsDefault(travelId, true)
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
                    delay(500)
                    repository.insertNewRecordImages(tempNewRecordImages.toList())
                }
                launch(Dispatchers.IO) {
                    repository.deleteRecordImageByTravelId(travelId)
                    delay(500)
                    repository.insertRecordImages(newRecordImages)
                }
            }

            withContext(Dispatchers.Main) {
                _recordImageList.value = newRecordImages
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

    /* 이미지 추가 기능 삭제 예정
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
     */

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

    /* 삭제 예정
    fun getIndexByGroupAndDay(group: Int, day: String): Int {
        val recordImageList = _recordImageList.value ?: return 0

        for ((index, recordImage) in recordImageList.withIndex()) {
            if (recordImage.group == group && recordImage.day == day) {
                return index
            }
        }

        return 0
    }
     */
}
