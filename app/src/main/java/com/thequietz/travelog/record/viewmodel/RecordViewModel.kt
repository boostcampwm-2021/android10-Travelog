package com.thequietz.travelog.record.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thequietz.travelog.data.RecordRepository
import com.thequietz.travelog.record.model.Record
import com.thequietz.travelog.record.model.RecordImage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class RecordViewModel @Inject constructor(
    private val repository: RecordRepository
) : ViewModel() {
    private var _recordList = MutableLiveData<List<Record>>()
    val recordList: LiveData<List<Record>> = _recordList

    init {
        // TODO("Schedule 테이블에서 데이터 로드 후 Record 테이블에 저장")
        viewModelScope.launch {
            val recordImages = withContext(Dispatchers.IO) {
                repository.loadRecordImages()
            }
            _recordList.value = createRecordFromRecordImages(recordImages)
        }
    }

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
}
