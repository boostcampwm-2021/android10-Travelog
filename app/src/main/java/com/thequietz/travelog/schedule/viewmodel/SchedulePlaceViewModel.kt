package com.thequietz.travelog.schedule.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thequietz.travelog.schedule.model.PlaceModel
import com.thequietz.travelog.schedule.repository.SchedulePlaceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SchedulePlaceViewModel @Inject constructor(
    private val repository: SchedulePlaceRepository
) : ViewModel() {
    private var _placeList = MutableLiveData<List<PlaceModel>>()
    val placeList: LiveData<List<PlaceModel>> = _placeList

    private var _selectedPlaces = MutableLiveData<MutableList<PlaceModel>>()
    val selectedPlaces: LiveData<MutableList<PlaceModel>> = _selectedPlaces

    private var _placeSelectedList = MutableLiveData<MutableList<PlaceModel>>()
    val placeSelectedList: LiveData<MutableList<PlaceModel>> = _placeSelectedList

    fun loadPlaceList() {
        viewModelScope.launch {
            _placeList.value = repository.loadPlaceList()
        }
    }

    fun searchPlaceList(keyword: String) {
        viewModelScope.launch {
            _placeList.value = repository.searchPlaceList(keyword)
        }
    }

    fun initPlaceSelectedList() {
        _placeSelectedList.value = mutableListOf()
        _selectedPlaces.value = mutableListOf()
    }

    fun addPlaceSelectedList(value: PlaceModel) {
        val isExisted = placeSelectedList.value?.find { it.cityName == value.cityName }
        if (isExisted != null) return

        _placeSelectedList.value?.add(value)
        _placeSelectedList.value = _placeSelectedList.value
        _selectedPlaces.value?.add(value)
        _selectedPlaces.value = _selectedPlaces.value
    }

    fun removePlaceSelectedList(cityName: String) {
        val updated = _placeSelectedList.value?.filter { it -> cityName != it.cityName }
        _placeSelectedList.value = updated?.toMutableList()

        _placeList.value?.forEach { it ->
            if (it.cityName == cityName) {
                it.isSelected = false
            }
        }
        _placeList.value = _placeList.value
    }
}
