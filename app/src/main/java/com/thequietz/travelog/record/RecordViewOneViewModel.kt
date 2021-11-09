package com.thequietz.travelog.record

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.thequietz.travelog.data.RepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RecordViewOneViewModel @Inject constructor(
    val repository: RepositoryImpl
) : ViewModel() {

    private val _imageList = MutableLiveData<List<RecordImage>>()
    val imageList: LiveData<List<RecordImage>> = _imageList

    private val _tempList = MutableLiveData<List<RecordImage>>()
    val tempList: LiveData<List<RecordImage>> = _tempList

    private val _currentImage = MutableLiveData<RecordImage>()
    val currentImage: LiveData<RecordImage> = _currentImage

    fun initImage() {
        var list = mutableListOf<RecordImage>()
        list.add(
            RecordImage().copy(
                schedule = "Day1",
                place = "석굴암",
                img = "https://tong.visitkorea.or.kr/cms/resource/67/2558467_image2_1.jpg",
                comment = "comment1"
            )
        )
        list.add(
            RecordImage().copy(
                schedule = "Day1",
                place = "제주도",
                img = "https://tong.visitkorea.or.kr/cms/resource/21/2689521_image2_1.jpg",
                comment = "comment2"
            )
        )
        list.add(
            RecordImage().copy(
                schedule = "Day2",
                place = "부산",
                img = "https://tong.visitkorea.or.kr/cms/resource/53/1253553_image2_1.jpg",
                comment = "comment3"
            )
        )
        list.add(
            RecordImage().copy(
                schedule = "Day3",
                place = "거제도",
                img = "https://tong.visitkorea.or.kr/cms/resource/22/1195422_image2_1.jpg",
                comment = "comment4"
            )
        )
        _tempList.value = list.toList()
        _currentImage.value = list.get(0)
    }
    fun setCurrentImage(position: Int) {
        _currentImage.value = _tempList.value?.get(position)
    }
}
