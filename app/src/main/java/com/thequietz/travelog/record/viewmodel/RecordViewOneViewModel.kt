package com.thequietz.travelog.record.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thequietz.travelog.data.RecordRepository
import com.thequietz.travelog.data.db.dao.JoinRecord
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

        var _currentJoinRecord = MutableLiveData<JoinRecord>()
        val currentJoinRecord: LiveData<JoinRecord> = _currentJoinRecord

        var currentTravleId: Int = 0
    }

    private val _dataList = MutableLiveData<List<JoinRecord>>()
    val dataList: LiveData<List<JoinRecord>> = _dataList

    private val _currentImage = MutableLiveData<JoinRecord>()
    val currentImage: LiveData<JoinRecord> = _currentImage

    private val _islistUpdate = MutableLiveData<Boolean>()
    val islistUpdate: LiveData<Boolean> = _islistUpdate

    var day: String = ""
    var place: String = ""
    var startInd = 0
    var imageId = 0
    var index = 0
    var from = ""

    init {
        startInd = 0
    }

    fun initVariable(args: RecordViewOneFragmentArgs) {
        day = args.day
        place = args.place
        currentTravleId = args.travelId
        imageId = args.index
        from = args.from
    }

    fun loadRecord() {
        val res = mutableListOf<JoinRecord>()
        viewModelScope.launch {
            val placeList = withContext(Dispatchers.IO) {
                repository.loadRecordImagesByTravelId(currentTravleId)
            }
            placeList.forEachIndexed { ind, it ->
                if (it.place == place) {
                    startInd = res.size
                }
                val temp = withContext(Dispatchers.IO) {
                    repository.loadJoinedRecordByTravelIdAndPlace(currentTravleId, it.place)
                }
                if (temp.size == 0) {
                    res.add(
                        withContext(Dispatchers.IO) {
                            repository.loadDefaultJoinedRecordByTravelId(currentTravleId, it.place)
                        }
                    )
                    if (res.last().newRecordImage.newRecordImageId == imageId) {
                        index = res.size - 1
                    }
                } else {
                    temp.forEachIndexed { ind, it ->
                        if (it.newRecordImage.newRecordImageId == imageId) {
                            index = res.size
                        }
                        res.add(it)
                    }
                }
            }
            _dataList.value = res
        }
    }

    fun setCurrentPosition(position: Int) {
        if (position < 0) {
            _currentPosition.value = 0
            dataList.value?.let {
                _currentJoinRecord.value = it.get(0)
                _currentImage.value = it.get(0)
                _currentJoinRecord.value = it.get(0)
            }
        } else {
            viewModelScope.launch {
                _currentPosition.value = position
                dataList.value?.let {
                    _currentJoinRecord.value = it.get(position)
                    _currentImage.value = it.get(position)
                    _currentJoinRecord.value = it.get(position)
                }
            }
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
                repository.updateCommentByImageId(comment, currentJoinRecord.value!!.newRecordImage.newRecordImageId)
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
                }
                currentJoinRecord.value?.newRecordImage?.let {
                    if (it.isDefault == false) {
                        repository.deleteNewRecordImage(it.newRecordImageId)
                        // currentPosition.value?.let { setCurrentPosition(it - 1) }
                    }
                }
            }
            withContext(Dispatchers.IO) {
                loadRecord()
            }
        }
    }
}
