package com.thequietz.travelog.record.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thequietz.travelog.record.model.Record
import com.thequietz.travelog.record.repository.RecordBasicRepository
import com.thequietz.travelog.schedule.model.ScheduleModel
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

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val scheduleList = repository.loadAllSchedule()
            val recordList = createRecordFromSchedule(scheduleList)

            // TODO: 데이터베이스 작업 완료 후 이미지 불러와서 보여줘야 한다.

            withContext(Dispatchers.Main) {
                _recordList.value = recordList
            }
        }
    }

    private fun createRecordFromSchedule(scheduleList: List<ScheduleModel>): List<Record> {
        val recordList = mutableListOf<Record>()

        for (schedule in scheduleList) {
            val record = Record(
                travelId = schedule.id,
                title = schedule.name,
                startDate = schedule.date.split('~').first(),
                endDate = schedule.date.split('~').last()
            )

            recordList.add(record)
        }

        return recordList
    }

    /*
    private fun createRecordFromRecordImages(recordImages: List<RecordImage>): List<Record> {
        val recordList = mutableListOf<Record>()
        var travelId = -1

        for (recordImage in recordImages) {
            val tempImageList = if (travelId != recordImage.travelId) {
                travelId = recordImage.travelId
                mutableListOf()
            } else {
                recordList.removeLast().images.toMutableList()
            }

            tempImageList.add(recordImage.url)

            recordList.add(
                Record(
                    recordImage.travelId,
                    recordImage.title,
                    recordImage.startDate,
                    recordImage.endDate,
                    tempImageList.toList()
                )
            )
        }

        return recordList.toList()
    }
     */
}
