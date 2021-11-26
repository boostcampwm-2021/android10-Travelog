package com.thequietz.travelog.record.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thequietz.travelog.data.RecordRepository
import com.thequietz.travelog.record.model.RecordImage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class RecordAddImageViewModel @Inject constructor(
    val repository: RecordRepository
) : ViewModel() {

    private val _imageList = MutableLiveData<List<RecordImage>>()
    val imageList: LiveData<List<RecordImage>> = _imageList

    private val _placeList = MutableLiveData<List<String>>()
    val placeList: LiveData<List<String>> = _placeList

    private val _scheduleList = MutableLiveData<List<String>>()
    val schedulList: LiveData<List<String>> = _scheduleList

    private val _travelName = MutableLiveData<String>()
    val travelName: LiveData<String> = _travelName

    private val _startDate = MutableLiveData<String>()
    val startDate: LiveData<String> = _startDate

    private val _endDate = MutableLiveData<String>()
    val endDate: LiveData<String> = _endDate

    private val _nextGroupId = MutableLiveData<Int>()
    val nextGroupId: LiveData<Int> = _nextGroupId

    var currentPlace = ""
    var currentSchedule = ""

    init {
        _imageList.value = listOf()
        initVariables()
    }
    fun initVariables() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val placeRes = repository.loadPlacesByTravelId(RecordViewOneViewModel.currentTravleId)
                val scheduleRes = repository.loadSchedulesByTravelId(RecordViewOneViewModel.currentTravleId)
                val groupRes = repository.loadNextGroupIdByTravelId(RecordViewOneViewModel.currentTravleId)
                val tempData = repository.loadOneDataByTravelId(RecordViewOneViewModel.currentTravleId)
                withContext(Dispatchers.Main) {
                    _placeList.value = placeRes
                    _scheduleList.value = scheduleRes
                    _travelName.value = tempData.title
                    _startDate.value = tempData.startDate
                    _endDate.value = tempData.endDate
                    _nextGroupId.value = groupRes.size + 1
                }
            }
        }
    }

    fun addImage(newImgList: List<RecordImage>) {
        viewModelScope.launch {
            imageList.value?.let {
                val res = it.toMutableList()
                withContext(Dispatchers.IO) {
                    newImgList.forEach { img ->
                        res.add(img)
                    }
                }
                _imageList.value = res
            }
        }
    }
    fun insertImages() {
        imageList.value?.let {
            repository.insertRecordImages(it)
        }
    }
}
