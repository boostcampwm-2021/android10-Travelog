package com.thequietz.travelog.record

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecordViewModel @Inject constructor() : ViewModel() {
    private var _recordList = MutableLiveData<List<Record>>()
    val recordList: LiveData<List<Record>> = _recordList

    init {
        viewModelScope.launch {
            // TODO: DB로부터 기록 목록 데이터 로드
        }
    }
}
