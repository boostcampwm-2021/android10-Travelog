package com.thequietz.travelog.ui.record.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thequietz.travelog.data.db.dao.NewRecordImage
import com.thequietz.travelog.ui.record.model.Record
import com.thequietz.travelog.ui.record.repository.RecordBasicRepository
import com.thequietz.travelog.ui.schedule.model.ScheduleModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class RecordViewModel @Inject constructor(
    private val repository: RecordBasicRepository
) : ViewModel() {
    private var _recordList = MutableLiveData<List<Record>>()
    val recordList: LiveData<List<Record>> = _recordList

    fun loadData() {
        viewModelScope.launch(Dispatchers.IO) {
            val scheduleList = repository.loadAllSchedule()
            val newImageList = repository.loadAllNewRecordImages()

            val recordList = createRecordFromSchedule(scheduleList, newImageList)

            withContext(Dispatchers.Main) {
                _recordList.value = recordList
            }
        }
    }

    private fun createRecordFromSchedule(
        scheduleList: List<ScheduleModel>,
        newImageList: List<NewRecordImage>
    ): List<Record> {
        val recordList = mutableListOf<Record>()
        val images = mutableListOf<String>()

        for (schedule in scheduleList) {
            for (newImage in newImageList) {
                if (newImage.newTravelId == schedule.id) {
                    images.add(newImage.url)
                }
            }

            val record = Record(
                travelId = schedule.id,
                title = schedule.name,
                startDate = schedule.date.split('~').first(),
                endDate = schedule.date.split('~').last(),
                images = images.toList()
            )

            recordList.add(record)
            images.clear()
        }

        return recordList
    }
}
