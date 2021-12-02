package com.thequietz.travelog.data

import android.content.Context

class SharedPreference(context: Context) {

    private val prefsFilename = "prefs"
    private val prefs = context.getSharedPreferences(prefsFilename, 0)

    /*fun disableGuideLoadingState(state: Boolean = true) {
        val editor = prefs.edit()
        editor.putBoolean("loading", state).apply()
        editor.apply()
    }
    fun loadGuideLoadingState() = prefs.getBoolean("loading", false)*/

    var alarmPermission: Boolean
        get() = prefs.getBoolean("alarm", false)
        set(value) = prefs.edit().putBoolean("alarm", value).apply()

    var scheduleAlarmPermission: Boolean
        get() = prefs.getBoolean("schedule_alarm", false)
        set(value) = prefs.edit().putBoolean("schedule_alarm", value).apply()

    var recordAlarmPermission: Boolean
        get() = prefs.getBoolean("record_alarm", false)
        set(value) = prefs.edit().putBoolean("record_alarm", value).apply()

    var scheduleTime: Int
        get() = prefs.getInt("schedule_time", 0)
        set(value) = prefs.edit().putInt("schedule_time", value).apply()

    var recordTime: Int
        get() = prefs.getInt("record_time", 0)
        set(value) = prefs.edit().putInt("record_time", value).apply()
}
