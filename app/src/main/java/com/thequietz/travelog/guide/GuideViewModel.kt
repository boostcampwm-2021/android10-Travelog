package com.thequietz.travelog.guide

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thequietz.travelog.data.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class GuideViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val _allPlaceList = MutableLiveData<List<Place>>()
    val allPlaceList: LiveData<List<Place>> = _allPlaceList

    private val _allRecommendPlaceList = MutableLiveData<List<RecommendPlace>>()
    val allRecommendPlaceList: LiveData<List<RecommendPlace>> = _allRecommendPlaceList

    fun setAllPlaceData() {
        viewModelScope.launch {
            val res = withContext(Dispatchers.IO) {
                repository.loadAllPlaceData()
            }
            _allPlaceList.value = res
        }
    }

    fun setRecommendPlaceData() {
        viewModelScope.launch {
            val res = withContext(Dispatchers.IO) {
                repository.loadRecommendPlaceData()
            }
            _allRecommendPlaceList.value = res
        }
    }
}
