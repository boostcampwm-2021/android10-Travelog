package com.thequietz.travelog.menu

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.thequietz.travelog.data.RepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MenuViewModel @Inject internal constructor(
    val repository: RepositoryImpl,
) : ViewModel() {

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
}
