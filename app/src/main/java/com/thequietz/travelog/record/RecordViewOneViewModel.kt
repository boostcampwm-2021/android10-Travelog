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

    private val _imageList = MutableLiveData<List<MyImage>>()
    val imageList: LiveData<List<MyImage>> = _imageList

    private val _tempList = MutableLiveData<List<MyImage>>()
    val tempList: LiveData<List<MyImage>> = _tempList

    private val _currentImage = MutableLiveData<MyImage>()
    val currentImage: LiveData<MyImage> = _currentImage

    fun initImage() {
        var list = mutableListOf<MyImage>()
        list.add(
            MyImage().copy(
                schedule = "Day1",
                place = "석굴암",
                img = "https://tong.visitkorea.or.kr/cms/resource/67/2558467_image2_1.jpg",
                comment = "coment1"
            )
        )
        list.add(
            MyImage().copy(
                schedule = "Day1",
                place = "제주도",
                img = "https://tong.visitkorea.or.kr/cms/resource/21/2689521_image2_1.jpg",
                comment = "comment2"
            )
        )
        list.add(
            MyImage().copy(
                schedule = "Day2",
                place = "부산",
                img = "https://tong.visitkorea.or.kr/cms/resource/53/1253553_image2_1.jpg",
                comment = "comment3"
            )
        )
        list.add(
            MyImage().copy(
                schedule = "Day3",
                place = "거제도",
                img = "https://tong.visitkorea.or.kr/cms/resource/22/1195422_image2_1.jpg",
                comment = "comment4"
            )
        )
        _tempList.value = list.toList()
        _currentImage.value = list.get(0)
        tempList.value?.forEach {
            println(it.toString())
        }
    }
    fun setCurrentImage(position: Int) {
        _currentImage.value = _tempList.value?.get(position)
    }
}
