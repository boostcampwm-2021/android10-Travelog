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

    init {
        val res = mutableListOf<TutorialImg>()
        res.add(
            TutorialImg().copy(
                url = Uri.parse("android.resource://com.thequietz.travelog/" + R.drawable.img_tutorial_menu).toString()
            )
        )
        res.add(
            TutorialImg().copy(
                url = Uri.parse("android.resource://com.thequietz.travelog/" + R.drawable.img_tutorial_schedule).toString()
            )
        )
        res.add(
            TutorialImg().copy(
                url = Uri.parse("android.resource://com.thequietz.travelog/" + R.drawable.img_tutorial_record).toString()
            )
        )
        res.add(
            TutorialImg().copy(
                url = Uri.parse("android.resource://com.thequietz.travelog/" + R.drawable.img_tutorial_menu).toString()
            )
        )
        _tutorialImgList.value = res
    }
}
