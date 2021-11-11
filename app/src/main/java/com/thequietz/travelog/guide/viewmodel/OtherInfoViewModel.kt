package com.thequietz.travelog.guide.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thequietz.travelog.data.RepositoryImpl
import com.thequietz.travelog.guide.Place
import com.thequietz.travelog.guide.RecommendPlace
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class OtherInfoViewModel @Inject internal constructor(
    val repository: RepositoryImpl
) : ViewModel() {

    private val _vacationSpotList = MutableLiveData<List<RecommendPlace>>()
    val vacationSpotList: LiveData<List<RecommendPlace>> = _vacationSpotList

    private val _foodList = MutableLiveData<List<RecommendPlace>>()
    val foodList: LiveData<List<RecommendPlace>> = _foodList

    private val _festivalList = MutableLiveData<List<RecommendPlace>>()
    val festivalList: LiveData<List<RecommendPlace>> = _festivalList

    private val _currentPlace = MutableLiveData<Place>()
    val currentPlace: LiveData<Place> = _currentPlace

    private val _vacationSpotList4 = MutableLiveData<List<RecommendPlace>>()
    val vacationSpotList4: LiveData<List<RecommendPlace>> = _vacationSpotList4

    private val _foodList4 = MutableLiveData<List<RecommendPlace>>()
    val foodList4: LiveData<List<RecommendPlace>> = _foodList4

    private val _festivalList4 = MutableLiveData<List<RecommendPlace>>()
    val festivalList4: LiveData<List<RecommendPlace>> = _festivalList4

    /*init {
        initCurrenetItem()
        initVacationSpotData()
        initFoodData()
        initFestivalData()
    }*/
    fun initCurrentItem(item: Place) {
        _currentPlace.value = item
    }

    fun initVacationSpotData() {
        viewModelScope.launch {
            val res = withContext(Dispatchers.IO) {
                repository.loadVacationSpotData(currentPlace.value!!.areaCode)
            }
            _vacationSpotList.value = res
            if (vacationSpotList.value!!.size < 4) {
                _vacationSpotList4.value = res.subList(0, vacationSpotList.value!!.size)
            } else {
                _vacationSpotList4.value = res.subList(0, 4)
            }
        }
    }

    fun initFoodData() {
        viewModelScope.launch {
            val res = withContext(Dispatchers.IO) {
                currentPlace.value?.let {
                    repository.loadFoodData(it.areaCode)
                }
                // repository.loadFoodData(currentPlace.value!!.areaCode)
            }
            _foodList.value = res!!
            if (foodList.value!!.size < 4) {
                _foodList4.value = res.subList(0, foodList.value!!.size)
            } else {
                _foodList4.value = res.subList(0, 4)
            }
        }
    }

    fun initFestivalData() {
        viewModelScope.launch {
            val res = withContext(Dispatchers.IO) {
                repository.loadFestivalData(currentPlace.value!!.areaCode)
            }
            _festivalList.value = res
            if (festivalList.value!!.size < 4) {
                _festivalList4.value = res.subList(0, festivalList.value!!.size)
            } else {
                _festivalList4.value = res.subList(0, 4)
            }
        }
    }
}
