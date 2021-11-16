package com.thequietz.travelog.record.viewmodel

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thequietz.travelog.data.RecordRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class RecordAddImageViewModel @Inject constructor(
    val repository: RecordRepository
) : ViewModel() {

    private val _imageList = MutableLiveData<List<NewImage>>()
    val imageList: LiveData<List<NewImage>> = _imageList

    init {
        _imageList.value = listOf()
    }
    fun addImage(img: NewImage) {
        viewModelScope.launch {
            var res = mutableListOf<NewImage>()
            imageList.value?.let {
                withContext(Dispatchers.IO) {
                    res = it.toMutableList()
                    res.add(img)
                }
                _imageList.value = res
            }
        }
    }
}
data class NewImage(
    val bitmap: Bitmap? = null,
    val place: String = "",
    val schedule: String = "",
)
