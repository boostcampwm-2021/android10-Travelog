package com.thequietz.travelog.schedule.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.thequietz.travelog.data.RepositoryImpl
import com.thequietz.travelog.util.dateToString
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class ScheduleSelectViewModel @Inject internal constructor(
    val repository: RepositoryImpl,
) : ViewModel() {

    private val _travelName = MutableLiveData<String?>()
    val travelName: LiveData<String?> = _travelName

    private val _startDate = MutableLiveData<String?>()
    val startDate: LiveData<String?> = _startDate

    private val _endDate = MutableLiveData<String?>()
    val endDate: LiveData<String?> = _endDate

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
    }

    fun setScheduleRange(startDate: Date, endDate: Date) {
        _startDate.postValue(dateToString(startDate))
        _endDate.postValue(dateToString(endDate))
        checkNextButtonEnable()
    }

    fun setTravelName(name: String) {
        _travelName.postValue(name)
        checkNextButtonEnable()
    }
}
