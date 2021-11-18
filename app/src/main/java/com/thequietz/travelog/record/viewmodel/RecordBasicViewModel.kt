package com.thequietz.travelog.record.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thequietz.travelog.record.model.RecordBasic
import com.thequietz.travelog.record.model.RecordBasicItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecordBasicViewModel @Inject constructor() : ViewModel() {
    private val _title = MutableLiveData<String>()
    val title: LiveData<String> = _title

    private val _date = MutableLiveData<String>()
    val date: LiveData<String> = _date

    private val _recordBasicItemList = MutableLiveData<List<RecordBasicItem>>()
    val recordBasicItemList: LiveData<List<RecordBasicItem>> = _recordBasicItemList

    init {
        // TODO:
        //  1. 여행 기록 목록 화면으로부터 데이터(제목, 기간)를 전달받고,
        //  2. DB로부터 기록 데이터 로드
        viewModelScope.launch {
            // 임시 데이터 사용
            val temp = RecordBasic(
                "경주 여행",
                "21.10.26",
                "21.10.28",
                listOf(
                    RecordBasicItem.TravelDestination("불국사", "2021.10.26", listOf()),
                    RecordBasicItem.TravelDestination("첨성대", "2021.10.26", listOf()),
                    RecordBasicItem.TravelDestination("황리단길", "2021.10.27", listOf()),
                    RecordBasicItem.TravelDestination("보문단지", "2021.10.27", listOf()),
                    RecordBasicItem.TravelDestination("황리단길2", "2021.10.28", listOf()),
                    RecordBasicItem.TravelDestination("보문단지2", "2021.10.28", listOf())
                )
            )

            _title.value = temp.title
            _date.value = temp.startDate
            _recordBasicItemList.value =
                createListOfRecyclerViewAdapterItem(temp.travelDestinations)
        }
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

        val tempRecordBasicItem = tempRecordBasicItemList[position] as RecordBasicItem.TravelDestination
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
