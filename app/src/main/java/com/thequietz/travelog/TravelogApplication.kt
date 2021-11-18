package com.thequietz.travelog

import android.app.Application
import com.thequietz.travelog.data.SharedPreference
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class TravelogApplication : Application() {
    companion object {
        lateinit var prefs: SharedPreference
    }

    override fun onCreate() {
        prefs = SharedPreference(applicationContext)
        super.onCreate()
    }
}
