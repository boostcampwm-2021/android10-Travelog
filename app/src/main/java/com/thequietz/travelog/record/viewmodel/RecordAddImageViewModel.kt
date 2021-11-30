package com.thequietz.travelog.record.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thequietz.travelog.data.RecordRepository
import com.thequietz.travelog.data.db.dao.NewRecordImage
import com.thequietz.travelog.record.model.PlaceAndSchedule
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class RecordAddImageViewModel @Inject constructor(
    val repository: RecordRepository
) : ViewModel() {

    private val _imageList = MutableLiveData<List<NewRecordImage>>()
    val imageList: LiveData<List<NewRecordImage>> = _imageList

    private val _placeAndScheduleList = MutableLiveData<List<PlaceAndSchedule>>()
    val placeAndScheduleList: LiveData<List<PlaceAndSchedule>> = _placeAndScheduleList

    private val _mainImageList = MutableLiveData<List<NewRecordImage>>()
    val mainImageList: LiveData<List<NewRecordImage>> = _mainImageList

    private val _travelName = MutableLiveData<String>()
    val travelName: LiveData<String> = _travelName

    private val _startDate = MutableLiveData<String>()
    val startDate: LiveData<String> = _startDate

    private val _endDate = MutableLiveData<String>()
    val endDate: LiveData<String> = _endDate

    private val _nextGroupId = MutableLiveData<Int>()
    val nextGroupId: LiveData<Int> = _nextGroupId

    private val _currentMainImage = MutableLiveData<NewRecordImage>()
    val currentMainImage: LiveData<NewRecordImage> = _currentMainImage

    var currentPlace = ""
    var currentSchedule = ""

    init {
        _imageList.value = listOf()
        initVariables()
    }
    fun initVariables() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val placeAndScheduleRes = repository.loadPlaceAndScheduleByTravelId(RecordViewOneViewModel.currentTravleId)
                val tempData = repository.loadOneDataByTravelId(RecordViewOneViewModel.currentTravleId)
                val mainImageRes = repository.loadMainImagesByTravelId(RecordViewOneViewModel.currentTravleId)
                mainImageRes.forEach { println(it.toString()) }
                withContext(Dispatchers.Main) {
                    _placeAndScheduleList.value = placeAndScheduleRes
                    _travelName.value = tempData.title
                    _startDate.value = tempData.startDate
                    _endDate.value = tempData.endDate
                    _mainImageList.value = mainImageRes
                }
            }
        }
    }

    fun addImage(newImgList: List<NewRecordImage>) {
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
            repository.insertNewRecordImages(it)
        }
    }

    fun setMainImage(position: Int) {
        mainImageList.value?.let {
            _currentMainImage.value = it.get(position)
        }
    }
}
