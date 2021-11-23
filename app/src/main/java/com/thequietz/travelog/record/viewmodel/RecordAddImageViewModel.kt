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

    init {
        _imageList.value = listOf()
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
    fun insertImage() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                imageList.value?.forEach {
                    repository.createRecordImage(it)
                }
            }
        }
    }
}
