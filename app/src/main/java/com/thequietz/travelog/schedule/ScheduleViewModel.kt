package com.thequietz.travelog.schedule

import androidx.lifecycle.ViewModel
import com.thequietz.travelog.data.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel()
