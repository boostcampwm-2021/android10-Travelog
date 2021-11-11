package com.thequietz.travelog.record

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thequietz.travelog.data.RecordRepository
import com.thequietz.travelog.util.SAMPLE_RECORD_IMAGES
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class RecordViewOneViewModel @Inject constructor(
    val repository: RecordRepository
) : ViewModel() {

    private val _imageList = MutableLiveData<List<RecordImage>>()
    val imageList: LiveData<List<RecordImage>> = this._imageList

    private val _currentImage = MutableLiveData<RecordImage>()
    val currentImage: LiveData<RecordImage> = _currentImage

    private val _islistUpdate = MutableLiveData<Boolean>()
    val islistUpdate: LiveData<Boolean> = _islistUpdate

    init {
        // createRecord()
        loadRecord()
        _currentPosition.value = 0
    }
    companion object {
        private val _currentPosition = MutableLiveData<Int>()
        val currentPosition: LiveData<Int> = _currentPosition
    }
    fun loadRecord() {
        viewModelScope.launch {
            val res = withContext(Dispatchers.IO) {
                repository.loadRecordImages()
            }
            withContext(Dispatchers.Main) {
                _imageList.postValue(res)
            }
        }
    }
    fun setCurrentImage(position: Int) {
        _currentImage.value = _imageList.value?.get(position)
    }

    fun createRecord() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                SAMPLE_RECORD_IMAGES.forEach {
                    repository.createRecordImage(it)
                }
            }
            withContext(Dispatchers.IO) {
                val res = repository.loadRecordImages()
                viewModelScope.launch {
                    _imageList.postValue(res)
                }
            }
        }
    }

    fun isCommentChanged(str: String): Boolean {
        if (currentImage.value?.comment != str) {
            return true
        }
        return false
    }

    fun updateComment(comment: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                viewModelScope.launch {
                    _islistUpdate.value = true
                }
                currentImage.value?.id?.let {
                    repository.updateRecordImageComment(comment, it)
                }
            }
            withContext(Dispatchers.IO) {
                loadRecord()
            }
        }
    }

    fun delete() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                viewModelScope.launch {
                    _islistUpdate.value = true
                    currentPosition.value?.let { setCurrentPosition(it - 1) }
                }
                currentImage.value?.id?.let {
                    repository.deleteRecordImage(it)
                }
            }
            withContext(Dispatchers.IO) {
                loadRecord()
            }
        }
    }

    fun setCurrentPosition(position: Int) {
        _currentPosition.value = position
    }

    fun resetIsListUpdate() {
        _islistUpdate.value = false
    }
}
