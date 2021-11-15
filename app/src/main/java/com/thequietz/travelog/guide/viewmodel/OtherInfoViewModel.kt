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

    private val _vacationSpotList = MutableLiveData<List<List<RecommendPlace>>>()
    val vacationSpotList: LiveData<List<List<RecommendPlace>>> = _vacationSpotList

    private val _foodList = MutableLiveData<List<List<RecommendPlace>>>()
    val foodList: LiveData<List<List<RecommendPlace>>> = _foodList

    private val _festivalList = MutableLiveData<List<List<RecommendPlace>>>()
    val festivalList: LiveData<List<List<RecommendPlace>>> = _festivalList

    private val _placeList = MutableLiveData<List<Place>>()
    val placeList: LiveData<List<Place>> = _placeList

    private val _vacationSpotList4 = MutableLiveData<List<RecommendPlace>>()
    val vacationSpotList4: LiveData<List<RecommendPlace>> = _vacationSpotList4

    private val _foodList4 = MutableLiveData<List<RecommendPlace>>()
    val foodList4: LiveData<List<RecommendPlace>> = _foodList4

    private val _festivalList4 = MutableLiveData<List<RecommendPlace>>()
    val festivalList4: LiveData<List<RecommendPlace>> = _festivalList4

    private val _currentPlace = MutableLiveData<Place>()
    val currentPlace: LiveData<Place> = _currentPlace

    private val _currentInd = MutableLiveData<Int>()
    val currentInd: LiveData<Int> = _currentInd

    /*init {
        initCurrenetItem()
        initVacationSpotData()
        initFoodData()
        initFestivalData()
    }*/
    fun initPlaceList(array: Array<Place>) {
        viewModelScope.launch {
            _placeList.value = array.toList()
            initVacationSpotData()
            initFoodData()
            initFestivalData()
            setCurrentInd(0)
        }
    }
    fun setCurrentInd(ind: Int) {
        viewModelScope.launch {
            _currentInd.value = ind
            _currentPlace.value = placeList.value?.get(ind)
            val vacationRes = vacationSpotList.value?.get(ind)
            vacationRes?.let {
                if (it.size < 4) {
                    _vacationSpotList4.value = it
                } else {
                    _vacationSpotList4.value = it.subList(0, 4)
                }
            }
            val foodRes = foodList.value?.get(ind)
            foodRes?.let {
                if (it.size < 4) {
                    _foodList4.value = it
                } else {
                    _foodList4.value = it.subList(0, 4)
                }
            }
            val festivalRes = festivalList.value?.get(ind)
            festivalRes?.let {
                if (it.size < 4) {
                    _festivalList4.value = it
                } else {
                    _festivalList4.value = it.subList(0, 4)
                }
            }
        }
    }

    fun initVacationSpotData() {
        viewModelScope.launch {
            val vacationRes = mutableListOf<List<RecommendPlace>>()
            placeList.value?.forEach {
                val tempRes = withContext(Dispatchers.IO) {
                    repository.loadVacationSpotData(it.areaCode)
                }
                vacationRes.add(tempRes)
            }
            _vacationSpotList.value = vacationRes
            val vacationTemp = vacationSpotList.value?.get(0)
            vacationTemp?.let {
                if (it.size < 4) {
                    _vacationSpotList4.value = it
                } else {
                    _vacationSpotList4.value = it.subList(0, 4)
                }
            }
        }
    }

    fun initFoodData() {
        viewModelScope.launch {
            val foodRes = mutableListOf<List<RecommendPlace>>()
            placeList.value?.forEach {
                val tempRes = withContext(Dispatchers.IO) {
                    repository.loadFoodData(it.areaCode)
                }
                foodRes.add(tempRes)
            }
            _foodList.value = foodRes
            val foodTemp = foodList.value?.get(0)
            foodTemp?.let {
                if (it.size < 4) {
                    _foodList4.value = it
                } else {
                    _foodList4.value = it.subList(0, 4)
                }
            }
        }
    }

    fun initFestivalData() {
        viewModelScope.launch {
            val festivalRes = mutableListOf<List<RecommendPlace>>()
            placeList.value?.forEach {
                val tempRes = withContext(Dispatchers.IO) {
                    repository.loadFestivalData(it.areaCode)
                }
                festivalRes.add(tempRes)
            }
            _festivalList.value = festivalRes
            val festivalTemp = festivalList.value?.get(0)
            festivalTemp?.let {
                if (it.size < 4) {
                    _festivalList4.value = it
                } else {
                    _festivalList4.value = it.subList(0, 4)
                }
            }
        }
    }
}
