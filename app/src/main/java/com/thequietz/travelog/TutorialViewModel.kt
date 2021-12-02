package com.thequietz.travelog

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.thequietz.travelog.data.RecordRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TutorialViewModel @Inject constructor(
    val repository: RecordRepository
) : ViewModel() {

    private val _tutorialImgList = MutableLiveData<List<TutorialImg>>()
    val tutorialImg: LiveData<List<TutorialImg>> = _tutorialImgList

    private val _currentImg = MutableLiveData<TutorialImg>()
    val currentImg: LiveData<TutorialImg> = _currentImg

    init {
        val res = mutableListOf<TutorialImg>()
        res.add(
            TutorialImg().copy(
                url = Uri.parse("android.resource://com.thequietz.travelog/" + R.drawable.img_tutorial_recommend).toString(),
                text = "선택한 지역에 대한 관광지, 먹거리, 행사 정보를 추천받을 수 있어요"
            )
        )
        res.add(
            TutorialImg().copy(
                url = Uri.parse("android.resource://com.thequietz.travelog/" + R.drawable.img_tutorial_specific_info).toString(),
                text = "검색한 장소에 대한 상세정보와 위치정보를 확인할 수 있어요"
            )
        )
        res.add(
            TutorialImg().copy(
                url = Uri.parse("android.resource://com.thequietz.travelog/" + R.drawable.img_tutorial_schedule).toString(),
                text = "추가한 일정들을 이용하여 여행 경로를 한눈에 확인할 수 있어요"
            )
        )
        res.add(
            TutorialImg().copy(
                url = Uri.parse("android.resource://com.thequietz.travelog/" + R.drawable.img_tutorial_record).toString(),
                text = "여행 중 찍은 이미지를 추가할 수 있고, 한줄 내용도 작성할 수 있어요"
            )
        )
        res.add(
            TutorialImg().copy(
                url = Uri.parse("android.resource://com.thequietz.travelog/" + R.drawable.img_tutorial_alarm).toString(),
                text = "알림설정을 통해 일정을 미리 확인하거나, 잊지 않고 여행 기록을 남길 수 있어요"
            )
        )
        _tutorialImgList.value = res
    }
    fun setCurrentImg(position: Int) {
        tutorialImg.value?.let {
            _currentImg.value = it.get(position)
        }
    }
}
