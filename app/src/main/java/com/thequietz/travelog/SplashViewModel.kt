package com.thequietz.travelog

import androidx.lifecycle.ViewModel
import com.thequietz.travelog.data.GuideRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject internal constructor(
    val guideRepository: GuideRepository
) : ViewModel() {

    fun caching() {
        println("splash caching")
        CoroutineScope(Dispatchers.IO).launch {
            guideRepository.loadAllPlaceData()
        }
    }
}
