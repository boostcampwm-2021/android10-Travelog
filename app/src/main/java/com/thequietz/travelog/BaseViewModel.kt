package com.thequietz.travelog

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers

open class BaseViewModel : ViewModel() {
    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Log.d("TAG", throwable.stackTraceToString())
    }

    protected val mainDispatchers = Dispatchers.Main + coroutineExceptionHandler
}
