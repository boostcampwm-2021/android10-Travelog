package com.thequietz.travelog.schedule

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.thequietz.travelog.data.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class ScheduleSelectViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val _travelName = MutableLiveData<String>()
    val travelName: LiveData<String> = _travelName

    private val _startDate = MutableLiveData<Date>()
    val startDate: LiveData<Date> = _startDate

    private val _endDate = MutableLiveData<Date>()
    val endDate: LiveData<Date> = _endDate

    fun setScheduleRange(startDate: Date, endDate: Date) {
        _startDate.postValue(startDate)
        _endDate.postValue(endDate)
    }
}
