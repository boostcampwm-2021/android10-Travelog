package com.thequietz.travelog.menu

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.thequietz.travelog.TravelogApplication
import com.thequietz.travelog.schedule.repository.ScheduleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MenuViewModel @Inject internal constructor(
    val repository: ScheduleRepository,
) : ViewModel() {

    private val pref = TravelogApplication.prefs

    private val _locationPermission = MutableLiveData<Boolean>()
    val locationPermission: LiveData<Boolean> = _locationPermission

    private val _alarmPermission = MutableLiveData<Boolean>()
    val alarmPermission: LiveData<Boolean> = _alarmPermission

    private val _scheduleAlarm = MutableLiveData<Boolean>()
    val scheduleAlarm: LiveData<Boolean> = _scheduleAlarm

    private val _recordAlarm = MutableLiveData<Boolean>()
    val recordAlarm: LiveData<Boolean> = _recordAlarm

    private val _scheduleAlarmTime = MutableLiveData<Int>()
    val scheduleAlarmTime: LiveData<Int> = _scheduleAlarmTime

    private val _recordAlarmTime = MutableLiveData<Int>()
    val recordAlarmTime: LiveData<Int> = _recordAlarmTime

    init {
        permissionSetting()
    }

    private fun permissionSetting() {
        _locationPermission.postValue(false)
        _alarmPermission.postValue(pref.alarmPermission)
        _scheduleAlarm.postValue(pref.alarmPermission)
        _recordAlarm.postValue(pref.recordAlarmPermission)
        _recordAlarmTime.value = (pref.recordTime)
        _scheduleAlarmTime.value = (pref.scheduleTime)
    }

    fun locationPermissionChange(isChecked: Boolean) {
        _locationPermission.postValue(isChecked)
    }

    fun alarmPermissionChange(isChecked: Boolean) {
        if (!isChecked) {
            _alarmPermission.postValue(false)
            pref.alarmPermission = false
        } else {
            _alarmPermission.postValue(true)
            pref.alarmPermission = true
        }
    }

    fun schedulePermissionChange(isChecked: Boolean) {
        _scheduleAlarm.postValue(isChecked)
        pref.scheduleAlarmPermission = isChecked
    }

    fun recordPermissionChange(isChecked: Boolean) {
        _recordAlarm.postValue(isChecked)
        pref.recordAlarmPermission = isChecked
    }

    fun scheduleTimeChange(index: Int) {
        _scheduleAlarmTime.postValue(index)
        pref.scheduleTime = index
    }

    fun recordTimeChange(index: Int) {
        _recordAlarmTime.postValue(index)
        pref.recordTime = index
    }

    private fun getTimeFromSpinner(flag: Int, index: Int): Int {
        return if (flag == 0) index + 6
        else index + 6 + 12
    }
}
