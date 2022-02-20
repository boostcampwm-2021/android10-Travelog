package com.thequietz.travelog.ui.guide.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thequietz.travelog.data.GuideRepository
import com.thequietz.travelog.ui.guide.Place
import com.thequietz.travelog.ui.guide.RecommendPlace
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class OtherInfoViewModel @Inject internal constructor(
    val repository: GuideRepository
) : ViewModel() {

    private val _vacationSpotList = MutableLiveData<List<RecommendPlace>>()
    val vacationSpotList: LiveData<List<RecommendPlace>> = _vacationSpotList

    private val _foodList = MutableLiveData<List<RecommendPlace>>()
    val foodList: LiveData<List<RecommendPlace>> = _foodList

    private val _festivalList = MutableLiveData<List<RecommendPlace>>()
    val festivalList: LiveData<List<RecommendPlace>> = _festivalList

    private val _currentPlace = MutableLiveData<Place>()
    val currentPlace: LiveData<Place> = _currentPlace

    var vacationPageInd = 0
    var foodPageInd = 0
    var festivalPageInd = 0

    private val _vacationPageEnd = MutableLiveData<Boolean>()
    val vacationPageEnd: LiveData<Boolean> = _vacationPageEnd

    private val _foodPageEnd = MutableLiveData<Boolean>()
    val foodPageEnd: LiveData<Boolean> = _foodPageEnd

    private val _festivalPageEnd = MutableLiveData<Boolean>()
    val festivalPageEnd: LiveData<Boolean> = _festivalPageEnd

    init {
        _vacationPageEnd.value = false
        _foodPageEnd.value = false
        _festivalPageEnd.value = false
        vacationPageInd = 1
        foodPageInd = 1
        festivalPageInd = 1
    }
    fun initPlace(place: Place) {
        viewModelScope.launch {
            _currentPlace.value = place
            initVacationSpotData()
            initFoodData()
            initFestivalData()
            println(currentPlace.value.toString())
        }
    }

    fun initVacationSpotData() {
        viewModelScope.launch {
            currentPlace.value?.let {
                val res = withContext(Dispatchers.IO) {
                    CategoryMap.get(Category.VACATION)?.let { type ->
                        repository.loadAreaData(it.areaCode.toString(), type, vacationPageInd)
                    }
                }
                res?.let {
                    _vacationSpotList.value = it
                }
            }
        }
    }

    fun initFoodData() {
        viewModelScope.launch {
            currentPlace.value?.let {
                val res = withContext(Dispatchers.IO) {
                    CategoryMap.get(Category.FOOD)?.let { type ->
                        repository.loadAreaData(it.areaCode.toString(), type, foodPageInd)
                    }
                }
                res?.let {
                    _foodList.value = it
                }
            }
        }
    }

    fun initFestivalData() {
        viewModelScope.launch {
            currentPlace.value?.let {
                val contentTypeId = 15
                val res = withContext(Dispatchers.IO) {
                    repository.loadFestivalData(it.areaCode.toString(), contentTypeId, festivalPageInd)
                }
                _festivalList.value = res
            }
        }
    }
    fun addVacationData() {
        viewModelScope.launch {
            vacationPageInd++
            val currentRes = vacationSpotList.value?.toMutableList()
            val res = withContext(Dispatchers.IO) {
                currentPlace.value?.let {
                    CategoryMap.get(Category.VACATION)?.let { type ->
                        repository.loadAreaData(it.areaCode.toString(), type, vacationPageInd)
                    }
                }
            }
            res?.forEach {
                currentRes?.add(it)
            }
            if (res?.size == 0) {
                _vacationPageEnd.value = true
            }
            currentRes?.let {
                _vacationSpotList.value = it
            }
        }
    }
    fun addFoodData() {
        viewModelScope.launch {
            foodPageInd++
            val currentRes = foodList.value?.toMutableList()
            val res = withContext(Dispatchers.IO) {
                currentPlace.value?.let {
                    CategoryMap.get(Category.FOOD)?.let { type ->
                        repository.loadAreaData(it.areaCode.toString(), type, foodPageInd)
                    }
                }
            }
            res?.forEach {
                currentRes?.add(it)
            }
            if (res?.size == 0) {
                _foodPageEnd.value = true
            }
            currentRes?.let {
                _foodList.value = it
            }
        }
    }
    fun addFestivalData() {
        viewModelScope.launch {
            festivalPageInd++
            val currentRes = festivalList.value?.toMutableList()
            val res = withContext(Dispatchers.IO) {
                currentPlace.value?.let {
                    val contentTypeId = 15
                    repository.loadFestivalData(it.areaCode.toString(), contentTypeId, festivalPageInd)
                }
            }
            res?.forEach {
                currentRes?.add(it)
            }
            if (res?.size == 0) {
                _festivalPageEnd.value = true
            }
            currentRes?.let {
                _festivalList.value = it
            }
        }
    }
    fun vacationAgain() {
        viewModelScope.launch {
            val res = withContext(Dispatchers.IO) {
                currentPlace.value?.let {
                    CategoryMap.get(Category.VACATION)?.let { type ->
                        repository.loadAreaData(it.areaCode.toString(), type, 1)
                    }
                }
            }
            res?.let {
                _vacationSpotList.value = it
            }
        }
    }
    fun foodAgain() {
        viewModelScope.launch {
            val res = withContext(Dispatchers.IO) {
                currentPlace.value?.let {
                    CategoryMap.get(Category.FOOD)?.let { type ->
                        repository.loadAreaData(it.areaCode.toString(), type, 1)
                    }
                }
            }
            res?.let {
                _foodList.value = it
            }
        }
    }
    fun festivalAgain() {
        viewModelScope.launch {
            val res = withContext(Dispatchers.IO) {
                currentPlace.value?.let {
                    val contentTypeId = 15
                    repository.loadFestivalData(it.areaCode.toString(), contentTypeId, 1)
                }
            }
            res?.let {
                _festivalList.value = it
            }
        }
    }
}
val CategoryMap = hashMapOf<Category, String>(
    Category.VACATION to "A01",
    Category.FOOD to "A05"
)
enum class Category {
    VACATION, FOOD
}
