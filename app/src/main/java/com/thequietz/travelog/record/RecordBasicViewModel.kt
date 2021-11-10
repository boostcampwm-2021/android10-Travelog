package com.thequietz.travelog.record

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecordBasicViewModel @Inject constructor() : ViewModel() {
    private val _title = MutableLiveData<String>()
    val title: LiveData<String> = _title

    private val _date = MutableLiveData<String>()
    val date: LiveData<String> = _date

    private val _travelDestinations = MutableLiveData<List<TravelDestination>>()
    val travelDestinations: LiveData<List<TravelDestination>> = _travelDestinations

    init {
        // TODO:
        //  1. 여행 기록 목록 화면으로부터 데이터(제목, 기간)를 전달받고,
        //  2. DB로부터 기록 데이터 로드
        // 임시 데이터 사용
        viewModelScope.launch {
            _title.value = "경주 여행"
            _date.value = "21.10.26 ~ 10.27"
            _travelDestinations.value = listOf(
                TravelDestination(
                    "불국사, 석굴암",
                    "2021.10.26",
                    listOf("", "", "")
                ),
                TravelDestination(
                    "첨성대",
                    "2021.10.26",
                    listOf("", "", "")
                ),
                TravelDestination(
                    "황리단길",
                    "2021.10.27",
                    listOf("", "", "")
                ),
                TravelDestination(
                    "보문단지",
                    "2021.10.27",
                    listOf("", "", "")
                )
            )
        }
    }
}
