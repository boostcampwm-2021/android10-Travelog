package com.thequietz.travelog.guide.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thequietz.travelog.data.GuideRepository
import com.thequietz.travelog.guide.model.Guide
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class GuideViewModel @Inject internal constructor(
    val guideRepository: GuideRepository
) : ViewModel() {

    private val _dataList = MutableLiveData<List<Guide>>()
    val dataList: LiveData<List<Guide>> = _dataList

    init {
        change2MyGuide()
    }

    private fun change2MyGuide() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val res = mutableListOf<Guide>()
                res.add(
                    Guide.Header().copy(
                        title = "추천 여행지역"
                    )
                )
                val recommendList = withContext(Dispatchers.IO) {
                    // repository.loadRecommendPlaceData()
                }
                res.add(
                    Guide.SpecificRecommend().copy(
                        // specificRecommendList = recommendList.toMutableList()
                    )
                )
                res.add(
                    Guide.Header().copy(
                        title = "여행지역 전체 보기"
                    )
                )
                val allDoSiList = withContext(Dispatchers.IO) {
                    guideRepository.loadAllDoSi()
                }
                res.add(
                    Guide.Dosi().copy(
                        specificDOSIList = allDoSiList.toMutableList()
                    )
                )
                withContext(Dispatchers.Main) {
                    _dataList.value = res
                }
            }
        }
    }
}
