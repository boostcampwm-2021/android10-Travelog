package com.thequietz.travelog.schedule.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.thequietz.travelog.data.GuideRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ScheduleSelectViewModel @Inject internal constructor(
    val guideRepository: GuideRepository,
) : ViewModel() {

    private val _travelName = MutableLiveData<String?>(null)
    val travelName: LiveData<String?> get() = _travelName

    private val _startDate = MutableLiveData<String?>(null)
    val startDate: LiveData<String?> get() = _startDate

    private val _endDate = MutableLiveData<String?>(null)
    val endDate: LiveData<String?> get() = _endDate

    private val _btnEnable = MutableLiveData(false)
    val btnEnable: LiveData<Boolean> get() = _btnEnable

    private fun checkNextButtonEnable() {
        val name = travelName.value
        val duration = startDate.value != null && endDate.value != null
        when (!name.isNullOrBlank() && duration) {
            true -> _btnEnable.value = true
            false -> _btnEnable.value = false
        }
    }

    fun setScheduleRange(startDate: String, endDate: String) {
        _startDate.value = startDate
        _endDate.value = endDate
        checkNextButtonEnable()
    }

    fun setTravelName(name: String) {
        _travelName.value = name
        checkNextButtonEnable()
    }
}
