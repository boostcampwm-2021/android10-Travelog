package com.thequietz.travelog.ui.schedule.viewmodel

import android.os.Parcelable
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thequietz.travelog.ui.schedule.model.SchedulePlaceModel
import com.thequietz.travelog.ui.schedule.repository.SchedulePlaceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SchedulePlaceViewModel @Inject constructor(
    private val repository: SchedulePlaceRepository
) : ViewModel() {
    private var _placeList = MutableLiveData<List<SchedulePlaceModel>>()
    val placeList: LiveData<List<SchedulePlaceModel>> = _placeList

    val handler = CoroutineExceptionHandler { _, throwable ->
        Log.d("ERROR", throwable.stackTraceToString())
        null
    }

    private var _placeSelectedList = MutableLiveData<MutableList<SchedulePlaceModel>>()
    val placeSelectedList: LiveData<MutableList<SchedulePlaceModel>> = _placeSelectedList

    private var _viewState = MutableLiveData<Parcelable>()
    val viewState: LiveData<Parcelable> get() = _viewState

    fun loadPlaceList() {
        viewModelScope.launch(Dispatchers.Main + handler) {
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
    }

    fun addPlaceSelectedList(value: SchedulePlaceModel) {
        val isExisted = placeSelectedList.value?.find { it.cityName == value.cityName }
        if (isExisted != null) return

        _placeSelectedList.value?.add(value)
        _placeSelectedList.value = _placeSelectedList.value
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

    fun saveViewState(viewState: Parcelable?) {
        val newViewState = viewState ?: return
        _viewState.value = newViewState
    }
}
