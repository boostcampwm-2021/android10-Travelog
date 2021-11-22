package com.thequietz.travelog.record.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thequietz.travelog.data.RecordRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecordViewManyInnerViewModel @Inject constructor(
    val repository: RecordRepository
) : ViewModel() {
    private val _deleteState = MutableLiveData<Boolean>()
    val deleteState: LiveData<Boolean> = _deleteState

    private val _checkedList = MutableLiveData<List<Int>>()
    val checkedList: LiveData<List<Int>> = _checkedList

    init {
        _deleteState.value = false
        _checkedList.value = mutableListOf()
    }
    fun changeDeleteState() {
        viewModelScope.launch {
            deleteState.value?.let {
                _deleteState.value = !it
            }
        }
        println("delete state ${deleteState.value}")
    }
    fun addChecked(id: Int) {
        viewModelScope.launch {
            checkedList.value?.let {
                val res = it.toMutableList()
                res.add(id)
                res.sort()
                _checkedList.value = res
            }
        }
    }
    fun deleteChecked(id: Int) {
        viewModelScope.launch {
            checkedList.value?.let {
                val res = it.toMutableList()
                var ind = 0
                res.forEachIndexed { idx, it ->
                    if (it == id) {
                        ind = idx
                        return@forEachIndexed
                    }
                }
                res.removeAt(ind)
                res.sort()
                _checkedList.value = res
            }
        }
    }
}
