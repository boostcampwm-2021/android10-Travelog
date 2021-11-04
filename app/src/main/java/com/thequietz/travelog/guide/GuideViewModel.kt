package com.thequietz.travelog.guide

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thequietz.travelog.data.RepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class GuideViewModel @Inject internal constructor(
    val repository: RepositoryImpl
) : ViewModel() {

    private val _allDoSiList = MutableLiveData<List<Place>>()
    val allDoSiList: LiveData<List<Place>> = _allDoSiList

    private val _allRecommendPlaceList = MutableLiveData<List<RecommendPlace>>()
    val allRecommendPlaceList: LiveData<List<RecommendPlace>> = _allRecommendPlaceList

    fun initAllDoSiData() {
        viewModelScope.launch {
            val res = withContext(Dispatchers.IO) {
                repository.loadAllDoSi()
            }
            _allDoSiList.value = res
        }
    }

    fun initRecommendPlaceData() {
        viewModelScope.launch {
            val res = withContext(Dispatchers.IO) {
                repository.loadRecommendPlaceData()
            }
            _allRecommendPlaceList.value = res
        }
    }
}
