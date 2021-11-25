package com.thequietz.travelog.record.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thequietz.travelog.data.RecordRepository
import com.thequietz.travelog.record.model.MyRecord
import com.thequietz.travelog.record.model.RecordImage
import com.thequietz.travelog.record.view.RecordViewManyFragmentArgs
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
        viewModelScope.launch {
            val loadData = withContext(Dispatchers.IO) {
                repository.loadRecordImagesByTravelId(args.travelId)
            }
            val res = mutableListOf<MyRecord>()
            if (loadData.size != 0) {
                var currentSchedule = loadData.get(0).day
                var currentPlace = loadData.get(0).place
                res.add(
                    MyRecord.RecordSchedule().copy(
                        date = currentSchedule
                    )
                )
                res.add(
                    MyRecord.RecordPlace().copy(
                        place = currentPlace
                    )
                )
                val currentImageList = mutableListOf<RecordImage>()
                loadData.forEach {
                    if (currentSchedule != it.day) { // 일정 다르면
                        res.add( // 이전 이미지 res에 넣어주고
                            MyRecord.RecordImageList().copy(
                                list = currentImageList.toMutableList()
                            )
                        )
                        currentImageList.clear() // 이미지 리스트 초기화

                        currentSchedule = it.day // 일정 갱신해주고
                        res.add( // res에 schedule넣어주고
                            MyRecord.RecordSchedule().copy(
                                date = currentSchedule
                            )
                        )
                        currentPlace = it.place // 장소 갱신해주고
                        res.add( // res에 Place넣어주고
                            MyRecord.RecordPlace().copy(
                                place = currentPlace
                            )
                        )
                        currentImageList.add(it) // 이미지 리스트에 현재 data 넣기
                    } else {
                        if (currentPlace != it.place) { // 일정 같은데, 장소 다르면
                            res.add( // 이전 이미지 res에 넣어주고
                                MyRecord.RecordImageList().copy(
                                    list = currentImageList.toMutableList()
                                )
                            )
                            currentImageList.clear() // 이미지리스트 초기화 시키고
                            currentPlace = it.place // 현재 place 갱신하고
                            res.add( // res에 현재 place 넣어주거
                                MyRecord.RecordPlace().copy(
                                    place = currentPlace
                                )
                            )
                            currentImageList.add(it) // 이미지 리스트에 현재 data 넣기
                        } else { // 일정, 장소 같으면
                            currentImageList.add(it) // image리스트에 현재 data 넣기
                        }
                    }
                }
                res.add( // 이미지 res에 넣어주고
                    MyRecord.RecordImageList().copy(
                        list = currentImageList.toMutableList()
                    )
                )
                withContext(Dispatchers.Main) {
                    _dataList.value = res
                }
            } else {
                withContext(Dispatchers.Main) {
                    _dataList.value = mutableListOf()
                }
            }
            _travelName.value = loadData.get(0).title
            _startDate.value = loadData.get(0).startDate
            _endDate.value = loadData.get(0).endDate
            loadData.forEach { println(it.toString()) }
        }
    }
}
