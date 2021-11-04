package com.thequietz.travelog.schedule.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.thequietz.travelog.data.RepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class ScheduleSelectViewModel @Inject internal constructor(
    val repository: RepositoryImpl
) : ViewModel() {

    private val _travelName = MutableLiveData<String?>()
    val travelName: LiveData<String?> = _travelName

    private val _startDate = MutableLiveData<Date?>()
    val startDate: LiveData<Date?> = _startDate

    private val _endDate = MutableLiveData<Date?>()
    val endDate: LiveData<Date?> = _endDate

    private val _btnEnable = MutableLiveData<Boolean>()
    val btnEnable: LiveData<Boolean> = _btnEnable

    init {
        _travelName.postValue(null)
        _startDate.postValue(null)
        _endDate.postValue(null)
        _btnEnable.postValue(false)
    }

    private fun checkNextButtonEnable() {
        _btnEnable.postValue(!(travelName.value.isNullOrBlank() || startDate.value == null || endDate.value == null))
        Log.e("button", travelName.value.toString())
    }

    fun setScheduleRange(startDate: Date, endDate: Date) {
        _startDate.postValue(startDate)
        _endDate.postValue(endDate)
        checkNextButtonEnable()
    }

    fun setTravelName(name: String) {
        _travelName.postValue(name)
        checkNextButtonEnable()
    }
}
