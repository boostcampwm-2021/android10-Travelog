package com.thequietz.travelog.guide.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thequietz.travelog.guide.model.GuideModel
import com.thequietz.travelog.guide.repository.GuideRepository
import kotlinx.coroutines.launch

class GuideViewModel: ViewModel() {
    private var _areaList = MutableLiveData<List<GuideModel>>()
    val areaList: LiveData<List<GuideModel>> = _areaList

    private val repository = GuideRepository()

    fun loadAreaList() {
        viewModelScope.launch {
            _areaList.value = repository.loadAreaList()
        }
    }
}
