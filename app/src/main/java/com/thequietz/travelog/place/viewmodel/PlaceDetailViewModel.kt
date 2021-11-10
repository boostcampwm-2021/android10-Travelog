package com.thequietz.travelog.place.viewmodel

import android.util.Log
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

    private var _name = MutableLiveData<String>()
    val name: LiveData<String> get() = _name

    fun loadPlaceDetail(placeId: String) {
        viewModelScope.launch {
            val data = repository.loadPlaceDetail(placeId)

            Log.d(TAG, data?.name ?: "NONE")
            _name.postValue(data?.name)
        }
    }
}
