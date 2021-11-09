package com.thequietz.travelog.record

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.thequietz.travelog.data.RepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class InnerViewModel @Inject constructor(
    val repository: RepositoryImpl
) : ViewModel() {

    private val _imageList = MutableLiveData<List<List<RecordImage>>>()
    val imageList: LiveData<List<List<RecordImage>>> = _imageList

    private val _currentList = MutableLiveData<List<RecordImage>>()
    val currentList: LiveData<List<RecordImage>> = _currentList

    fun setCurrentData(list: MutableList<RecordImage>) {
        _currentList.value = list
    }
}
