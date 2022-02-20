package com.thequietz.travelog.ui.menu

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.thequietz.travelog.TravelogApplication
import com.thequietz.travelog.ui.menu.alarm.AlarmType
import com.thequietz.travelog.ui.menu.alarm.cancelAlarm
import com.thequietz.travelog.ui.menu.alarm.registerAlarm
import com.thequietz.travelog.ui.schedule.repository.ScheduleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class MenuViewModel @Inject internal constructor(
    @ApplicationContext private val context: Context,
    val repository: ScheduleRepository,
) : ViewModel() {

    private val pref = TravelogApplication.prefs

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
        _alarmPermission.value = pref.alarmPermission
        _scheduleAlarm.value = pref.scheduleAlarmPermission
        _recordAlarm.value = pref.recordAlarmPermission
        _recordAlarmTime.value = pref.recordTime
        _scheduleAlarmTime.value = pref.scheduleTime
    }

    fun alarmPermissionChange(isChecked: Boolean) {
        if (!isChecked) {
            _alarmPermission.postValue(false)
            pref.alarmPermission = false
            cancelRecordAlarm()
            cancelScheduleAlarm()
        } else {
            _alarmPermission.postValue(true)
            pref.alarmPermission = true
            if (scheduleAlarm.value == true) {
                makeScheduleAlarms()
            }
            if (recordAlarm.value == true) {
                makeRecordAlarms()
            }
        }
    }

    fun schedulePermissionChange(isChecked: Boolean) {
        _scheduleAlarm.value = isChecked
        pref.scheduleAlarmPermission = isChecked
        if (isChecked) makeScheduleAlarms()
        else cancelScheduleAlarm()
    }

    fun recordPermissionChange(isChecked: Boolean) {
        _recordAlarm.postValue(isChecked)
        pref.recordAlarmPermission = isChecked
        if (isChecked) makeRecordAlarms()
        else cancelRecordAlarm()
    }

    fun scheduleTimeChange(index: Int) {
        _scheduleAlarmTime.value = index
        pref.scheduleTime = index
        cancelScheduleAlarm()
        makeScheduleAlarms()
    }

    fun recordTimeChange(index: Int) {
        _recordAlarmTime.value = index
        pref.recordTime = index
        cancelRecordAlarm()
        makeRecordAlarms()
    }

    private fun makeScheduleAlarms() {
        registerAlarm(context, AlarmType.Schedule, getScheduleTime())
    }

    private fun makeRecordAlarms() {
        registerAlarm(context, AlarmType.Record, getRecordTime())
    }

    private fun cancelScheduleAlarm() {
        cancelAlarm(context, AlarmType.Schedule)
    }

    private fun cancelRecordAlarm() {
        cancelAlarm(context, AlarmType.Record)
    }

    private fun getScheduleTime(): String {
        var time = ""
        scheduleAlarmTime.value?.let { time = getTimeFromSpinner(0, it) }
        return time
    }

    private fun getRecordTime(): String {
        var time = ""
        recordAlarmTime.value?.let { time = getTimeFromSpinner(1, it) }
        return time
    }

    private fun getTimeFromSpinner(flag: Int, index: Int): String {
        val time = if (flag == 0) index + 6
        else index + 6 + 12

        var str = time.toString()
        if (time < 10) str = "0$str"
        return "$str:00:00"
    }
}
