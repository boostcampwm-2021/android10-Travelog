package com.thequietz.travelog.schedule.viewmodel

import androidx.lifecycle.ViewModel
import com.thequietz.travelog.data.ScheduleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ScheduleDetailViewModel @Inject internal constructor(
    private val repository: ScheduleRepository,
) : ViewModel()
