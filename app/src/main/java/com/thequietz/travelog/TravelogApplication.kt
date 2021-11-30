package com.thequietz.travelog

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
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
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }
}
