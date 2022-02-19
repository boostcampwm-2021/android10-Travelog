package com.thequietz.travelog.ui.record.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thequietz.travelog.data.RecordRepository
import com.thequietz.travelog.data.db.dao.JoinRecord
import com.thequietz.travelog.ui.record.model.MyRecord
import com.thequietz.travelog.ui.record.view.RecordViewManyFragmentArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class RecordViewManyViewModel @Inject constructor(
    val repository: RecordRepository
) : ViewModel() {
    private val _dataList = MutableLiveData<List<MyRecord>>()
    val dataList: LiveData<List<MyRecord>> = _dataList

    private val _travelName = MutableLiveData<String>()
    val travelName: LiveData<String> = _travelName

    private val _startDate = MutableLiveData<String>()
    val startDate: LiveData<String> = _startDate

    private val _endDate = MutableLiveData<String>()
    val endDate: LiveData<String> = _endDate

    init {
        // change2MyRecord()
    }

    fun change2MyRecord(args: RecordViewManyFragmentArgs) {
        val res = mutableListOf<MyRecord>()

        viewModelScope.launch {
            val placeList = withContext(Dispatchers.IO) {
                repository.loadRecordImagesByTravelId(args.travelId)
            }
            placeList.forEach {
                val temp = withContext(Dispatchers.IO) {
                    repository.loadJoinedRecordByTravelIdAndPlace(args.travelId, it.place)
                }
                if (temp.isEmpty()) {
                    val anotherTemp = withContext(Dispatchers.IO) {
                        repository.loadDefaultJoinedRecordByTravelId(args.travelId, it.place)
                    }
                    if (anotherTemp != null) {
                        res.add(
                            MyRecord.RecordSchedule().copy(
                                day = anotherTemp.recordImage.day
                            )
                        )
                        res.add(
                            MyRecord.RecordPlace().copy(
                                place = anotherTemp.recordImage.place
                            )
                        )
                    }
                    val resList = mutableListOf<JoinRecord>()
                    resList.add(anotherTemp)
                    res.add(
                        MyRecord.RecordImageList().copy(
                            list = resList
                        )
                    )
                } else {
                    res.add(
                        MyRecord.RecordSchedule().copy(
                            day = temp.get(0).recordImage.day
                        )
                    )
                    res.add(
                        MyRecord.RecordPlace().copy(
                            place = temp.get(0).recordImage.place
                        )
                    )
                    res.add(
                        MyRecord.RecordImageList().copy(
                            list = temp.toMutableList()
                        )
                    )
                }
            }
            _dataList.value = res
            _travelName.value = placeList.get(0).title
            _startDate.value = placeList.get(0).startDate
            _endDate.value = placeList.get(0).endDate
        }
    }
}
