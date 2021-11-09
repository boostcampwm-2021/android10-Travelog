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
            // 임시 데이터 사용
            _recordList.value = listOf(
                Record("제주도여행", "2021.08.30", "2021.09.01", null),
                Record("경주여행", "2021.10.26", "2021.10.28", null),
                Record("강릉여행", "2021.12.20", "2021.12.25", null)
            )
        }
    }
}
