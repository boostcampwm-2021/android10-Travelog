package com.thequietz.travelog.ui.record.viewmodel

/*
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thequietz.travelog.data.RecordRepository
import com.thequietz.travelog.ui.record.model.RecordImage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class RecordAddViewModel @Inject constructor(
    private val repository: RecordRepository
) : ViewModel() {
    private val tempRecordImage = MutableLiveData<RecordImage>()
    private val tempGroup = MutableLiveData<Int>()

    private val _place = MutableLiveData<String>()
    val place: LiveData<String> = _place

    private val _day = MutableLiveData<String>()
    val day: LiveData<String> = _day

    private val _imageList = MutableLiveData<List<String>>()
    val imageList: LiveData<List<String>> = _imageList

    fun createRecord(travelId: Int, day: String) {
        viewModelScope.launch(Dispatchers.IO) {
            // val recordImage = repository.loadLastRecordImageByTravelIdAndDay(travelId, day)
            // val group = repository.loadGroupFromRecordImageByTravelId(travelId)

            withContext(Dispatchers.Main) {
                tempRecordImage.value = recordImage
                tempGroup.value = group
                _day.value = recordImage.day
            }
        }
    }

    fun addImage(uri: String) {
        val tempImageList = _imageList.value ?: listOf()

        _imageList.value = tempImageList.toMutableList().run {
            add(uri)
            toList()
        }
    }

    fun addRecord() {
        val temp = tempRecordImage.value ?: return
        println("temp 통과")
        val tempGroup = tempGroup.value ?: return
        println("tempGroup 통과")
        // val newPlace = _place.value ?: return
        val newImageList = _imageList.value ?: listOf()
        println("newImageList 통과")
        val newRecordImages = mutableListOf<RecordImage>()
        println("newRecordImages 통과")

        for (image in newImageList) {
            newRecordImages.add(
                RecordImage(
                    travelId = temp.travelId,
                    title = temp.title,
                    startDate = temp.startDate,
                    endDate = temp.endDate,
                    day = temp.day,
                    // place = newPlace,
                    place = "test",
                )
            )
        }

        newRecordImages.forEach {
            println(it)
        }

        viewModelScope.launch(Dispatchers.IO) {
            repository.insertRecordImages(newRecordImages)
        }
    }
}
*/
