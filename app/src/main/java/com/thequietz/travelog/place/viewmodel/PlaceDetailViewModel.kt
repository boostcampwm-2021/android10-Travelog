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
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class PlaceDetailViewModel @Inject constructor(
    private val repository: PlaceDetailRepository
) : ViewModel() {
    private val TAG = "VIEWMODEL"
    private val converter = SimpleDateFormat("yyyy.MM.dd", Locale.KOREAN)
    private var _detail = MutableLiveData<PlaceDetailModel>()
    val detail: LiveData<PlaceDetailModel> get() = _detail

    private var _operation = MutableLiveData<String>()
    val operation: LiveData<String> get() = _operation

    private var _review = MutableLiveData<String>()
    val review: LiveData<String> get() = _review


    fun loadPlaceDetail(placeId: String) {
        viewModelScope.launch {
            val data = repository.loadPlaceDetail(placeId)

            data.let {
                Log.d(TAG, it?.photos?.size.toString())
                _detail.value = it
            }
            _operation.value = data?.operation?.operation?.fold("", { acc, value ->
                val newValue = acc + value + "\n"
                newValue
            })
            _review.value = data?.reviews?.fold("", { acc, value ->
                acc + value.text + "\n" + converter.format(Date(value.time * 1000)) + "\n\n"
            })
        }
    }
}
