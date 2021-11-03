package com.thequietz.travelog.schedule.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thequietz.travelog.schedule.model.SelectModel
import com.thequietz.travelog.schedule.model.SelectedData
import com.thequietz.travelog.schedule.repository.SelectRepository
import kotlinx.coroutines.launch

class SelectViewModel : ViewModel() {
    private var _areaList = MutableLiveData<List<SelectModel>>()
    val areaList: LiveData<List<SelectModel>> = _areaList

    private var _selectedList = MutableLiveData<MutableList<SelectedData>>()
    val selectedDataList: LiveData<MutableList<SelectedData>> = _selectedList

    private val repository = SelectRepository()

    fun loadAreaList() {
        viewModelScope.launch {
            _areaList.value = repository.loadAreaList()
        }
    }

    fun initSelectedList() {
        _selectedList.value = mutableListOf()
    }

    fun addSelectedList(index: Int, value: SelectModel) {
        val isExisted = selectedDataList.value?.find { it.value == value.cityName }
        if (isExisted != null) return

        val guideSelected = SelectedData(index, value.cityName)
        _selectedList.value?.add(guideSelected)
        _selectedList.value = _selectedList.value
    }

    fun removeSelectedList(position: Int) {
        val updated = _selectedList.value?.filterIndexed { index, _ -> position != index }
        _selectedList.value = updated?.toMutableList()
    }
}
