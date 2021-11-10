package com.thequietz.travelog.place.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thequietz.travelog.place.model.PlaceSearchModel
import com.thequietz.travelog.place.repository.PlaceSearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlaceSearchViewModel @Inject constructor(
    private val repository: PlaceSearchRepository
) : ViewModel() {
    private var _place = MutableLiveData<List<PlaceSearchModel>>()
    val place: LiveData<List<PlaceSearchModel>> get() = _place

    val query = MutableLiveData<String>()

    fun initPlaceList() {
        _place.value = listOf()
    }

    fun loadPlaceList(query: String) {
        viewModelScope.launch {
            val placeList = repository.loadPlaceList(query)
            _place.value = placeList
        }
    }

    fun onClickBackButton() {
        _place.value = listOf()
    }
}
