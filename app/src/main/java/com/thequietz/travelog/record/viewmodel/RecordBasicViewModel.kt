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

    fun loadData(title: String) {
        viewModelScope.launch {
            val recordImages = withContext(Dispatchers.IO) {
                repository.loadRecordImagesByTitle(title)
            }

            val recordBasic = createRecordBasicFromRecordImages(recordImages)

            _title.value = recordBasic.title
            _date.value = recordBasic.startDate
            _recordBasicItemList.value =
                createListOfRecyclerViewAdapterItem(recordBasic.travelDestinations)
        }
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
                        recordImageList.toList()
                    )
                )
                recordImageList.clear()
            }
        }

        return RecordBasic(
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
        tempImageList.add(uri.toString())

        val item = RecordBasicItem.TravelDestination(
            tempRecordBasicItem.name,
            tempRecordBasicItem.date,
            tempImageList.toList()
        )

        val tempRecordBasicItemMutableList = tempRecordBasicItemList.toMutableList()
        tempRecordBasicItemMutableList[position] = item
        _recordBasicItemList.value = tempRecordBasicItemMutableList.toList()
    }

    fun deleteRecord(position: Int) {
        val tempRecordBasicItemList = _recordBasicItemList.value ?: return
        val tempRecordBasicItemMutableList = tempRecordBasicItemList.toMutableList()

        tempRecordBasicItemMutableList.getOrNull(position) ?: return
        tempRecordBasicItemMutableList.removeAt(position)

        _recordBasicItemList.value = tempRecordBasicItemMutableList.toList()
    }
}
