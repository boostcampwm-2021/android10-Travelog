package com.thequietz.travelog.record.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thequietz.travelog.data.RecordRepository
import com.thequietz.travelog.record.model.RecordImage
import com.thequietz.travelog.record.view.RecordViewOneFragmentArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class RecordViewOneViewModel @Inject constructor(
    val repository: RecordRepository
) : ViewModel() {

    companion object {
        private val _currentPosition = MutableLiveData<Int>()
        val currentPosition: LiveData<Int> = _currentPosition
        var currentTravleId: Int = 0
    }

    private val _imageList = MutableLiveData<List<RecordImage>>()
    val imageList: LiveData<List<RecordImage>> = this._imageList

    private val _currentImage = MutableLiveData<RecordImage>()
    val currentImage: LiveData<RecordImage> = _currentImage

    private val _islistUpdate = MutableLiveData<Boolean>()
    val islistUpdate: LiveData<Boolean> = _islistUpdate

    var travelId: Int = 0
    var groupId: Int = 0
    var day: String = ""

    init {
        // createRecord()
        // loadRecord()
        _currentPosition.value = 0
    }
    fun initVariable(args: RecordViewOneFragmentArgs) {
        travelId = args.travelId
        groupId = args.group
        day = args.day
    }
    fun loadRecord() {
        viewModelScope.launch {
            val res = withContext(Dispatchers.IO) {
                repository.loadRecordImagesByTravelId(travelId)
            }
            withContext(Dispatchers.Main) {
                _imageList.value = res
            }
            println("viewOneFragment  ${imageList.value?.size}")
            // setCurrentImage(currentInd)
        }
    }

    fun setCurrentImage(position: Int) {
        _currentImage.value = _imageList.value?.get(position)
    }

    fun isCommentChanged(str: String): Boolean {
        if (currentImage.value?.comment != str) {
            return true
        }
        return false
    }

    fun setCurrentPosition(position: Int) {
        if (position <0) {
            return
        } else {
            _currentPosition.value = position
        }
    }

    fun resetIsListUpdate() {
        _islistUpdate.value = false
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
}
