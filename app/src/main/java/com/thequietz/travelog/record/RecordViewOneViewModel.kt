package com.thequietz.travelog.record

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thequietz.travelog.data.RecordRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class RecordViewOneViewModel @Inject constructor(
    val repository: RecordRepository
) : ViewModel() {

    private val _imageList = MutableLiveData<List<RecordImage>>()
    val imageList: LiveData<List<RecordImage>> = this._imageList

    private val _currentImage = MutableLiveData<RecordImage>()
    val currentImage: LiveData<RecordImage> = _currentImage

    init {
        // createRecord()
        loadRecord()
    }
    fun loadRecord() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val res = repository.loadRecordImages()
                withContext(Dispatchers.Main) {
                    _imageList.value = res
                }
            }
        }
    }
    fun setCurrentImage(position: Int) {
        _currentImage.value = _imageList.value?.get(position)
    }

    fun createRecord() {
        /*tempData.forEach{
            repository.createRecordImage(it)
        }*/
        /*tempData.forEach{
            println(it.toString())
            //repository.createRecordImage(it)
        }*/

        /*viewModelScope.launch {
            withContext(Dispatchers.IO){

            }
            loadRecord()
        }*/
    }
}
