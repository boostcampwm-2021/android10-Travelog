package com.thequietz.travelog.place.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.thequietz.travelog.common.BaseViewModel
import com.thequietz.travelog.place.model.PlaceSearchModel
import com.thequietz.travelog.place.repository.PlaceSearchRepository
import com.thequietz.travelog.schedule.model.SchedulePlaceModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlaceSearchViewModel @Inject constructor(
    private val repository: PlaceSearchRepository
) : BaseViewModel() {
    private var _place = MutableLiveData<List<PlaceSearchModel>>()
    val place: LiveData<List<PlaceSearchModel>> get() = _place

    private var _location = MutableLiveData<SchedulePlaceModel>()
    val location: LiveData<SchedulePlaceModel> get() = _location

    val query = MutableLiveData<String>()

    fun selectLocation(model: SchedulePlaceModel) {
        _location.value = model
    }

    fun initPlaceList() {
        _place.value = listOf()
    }

    fun loadPlaceList(query: String, lat: Double, lng: Double) {
        viewModelScope.launch(mainDispatchers) {
            val placeList = repository.loadPlaceList(query, lat, lng)

            _place.value = placeList
        }
    }
}
