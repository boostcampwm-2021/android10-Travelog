package com.thequietz.travelog.place.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thequietz.travelog.place.model.PlaceDetailModel
import com.thequietz.travelog.place.repository.PlaceDetailRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlaceDetailViewModel @Inject constructor(
    private val repository: PlaceDetailRepository
) : ViewModel() {
    private val TAG = "VIEWMODEL"
    private var _detail = MutableLiveData<PlaceDetailModel>()
    val detail: LiveData<PlaceDetailModel> get() = _detail

    fun loadPlaceDetail(placeId: String) {
        viewModelScope.launch {
            val data = repository.loadPlaceDetail(placeId)

            data.let { it ->
                _detail.value = it
            }
        }
    }
}
