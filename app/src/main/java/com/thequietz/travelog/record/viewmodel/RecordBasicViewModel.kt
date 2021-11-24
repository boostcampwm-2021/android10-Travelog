package com.thequietz.travelog.record.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thequietz.travelog.data.RecordRepository
import com.thequietz.travelog.record.model.RecordBasic
import com.thequietz.travelog.record.model.RecordBasicItem
import com.thequietz.travelog.record.model.RecordImage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class RecordBasicViewModel @Inject constructor(
    private val repository: RecordRepository
) : ViewModel() {
    private val _title = MutableLiveData<String>()
    val title: LiveData<String> = _title

    private val _date = MutableLiveData<String>()
    val date: LiveData<String> = _date

    private val _recordBasicItemList = MutableLiveData<List<RecordBasicItem>>()
    val recordBasicItemList: LiveData<List<RecordBasicItem>> = _recordBasicItemList

    private val _recordImageList = MutableLiveData<List<RecordImage>>()
    val recordImageList: LiveData<List<RecordImage>> = _recordImageList

    fun loadData(travelId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val recordImages = repository.loadRecordImagesByTravelId(travelId)

            recordImages.forEach {
                println(it)
            }

            withContext(Dispatchers.Main) {
                _recordImageList.value = recordImages
            }
        }
    }

    fun createData() {
        val recordImages = _recordImageList.value ?: return
        val recordBasic = createRecordBasicFromRecordImages(recordImages)
        _title.value = recordBasic.title
        _date.value = recordBasic.startDate
        _recordBasicItemList.value =
            createListOfRecyclerViewAdapterItem(recordBasic.travelDestinations)
    }

    private fun createRecordBasicFromRecordImages(recordImages: List<RecordImage>): RecordBasic {
        val recordImageList = mutableListOf<String>()
        val recordDestinationList = mutableListOf<RecordBasicItem.TravelDestination>()

        for (i in recordImages.indices) {
            recordImageList.add(recordImages[i].url)

            if ((i + 1 < recordImages.size && recordImages[i].place != recordImages[i + 1].place) ||
                (i == recordImages.lastIndex && recordImages[i - 1].place != recordImages[i].place)
            ) {
                recordDestinationList.add(
                    RecordBasicItem.TravelDestination(
                        recordImages[i].place,
                        recordImages[i].schedule,
                        recordImages[i].group,
                        recordImageList.toList()
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

    private fun createListOfRecyclerViewAdapterItem(travelDestinations: List<RecordBasicItem.TravelDestination>): List<RecordBasicItem> {
        val list = mutableListOf<RecordBasicItem>()
        var currDate = ""
        var day = 0

        for (travelDestination in travelDestinations) {
            if (currDate != travelDestination.date) {
                currDate = travelDestination.date
                day++
                list.add(RecordBasicItem.RecordBasicHeader("Day$day", currDate))
            }
            list.add(travelDestination)
        }

        return list.toList()
    }

    fun addImage(uri: Uri, position: Int) {
        val tempRecordBasicItemList = _recordBasicItemList.value ?: return
        tempRecordBasicItemList.getOrNull(position) ?: return

        val tempRecordBasicItem =
            tempRecordBasicItemList[position] as RecordBasicItem.TravelDestination
        val tempImageList = tempRecordBasicItem.images.toMutableList()
        val imageUrl = uri.toString()
        tempImageList.add(imageUrl)

        val item = RecordBasicItem.TravelDestination(
            tempRecordBasicItem.name,
            tempRecordBasicItem.date,
            tempRecordBasicItem.group,
            tempImageList.toList()
        )

        val tempRecordBasicItemMutableList = tempRecordBasicItemList.toMutableList()
        tempRecordBasicItemMutableList[position] = item
        _recordBasicItemList.value = tempRecordBasicItemMutableList.toList()

        viewModelScope.launch(Dispatchers.IO) {
            val tempRecordImageList = _recordImageList.value ?: return@launch
            val tempRecordImage =
                tempRecordImageList.find { it.place == item.name } ?: return@launch

            repository.insertRecordImage(
                RecordImage(
                    travelId = tempRecordImage.travelId,
                    title = tempRecordImage.title,
                    startDate = tempRecordImage.startDate,
                    endDate = tempRecordImage.endDate,
                    schedule = tempRecordImage.schedule,
                    place = tempRecordImage.place,
                    url = imageUrl,
                    group = tempRecordImage.group
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
}
