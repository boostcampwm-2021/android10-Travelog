package com.thequietz.travelog.guide.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thequietz.travelog.data.GuideRepository
import com.thequietz.travelog.guide.Place
import com.thequietz.travelog.guide.RecommendPlace
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class OtherInfoViewModel @Inject internal constructor(
    val repository: GuideRepository
) : ViewModel() {

    private val _vacationSpotList = MutableLiveData<List<List<RecommendPlace>>>()
    val vacationSpotList: LiveData<List<List<RecommendPlace>>> = _vacationSpotList

    private val _foodList = MutableLiveData<List<List<RecommendPlace>>>()
    val foodList: LiveData<List<List<RecommendPlace>>> = _foodList

    private val _festivalList = MutableLiveData<List<List<RecommendPlace>>>()
    val festivalList: LiveData<List<List<RecommendPlace>>> = _festivalList

    private val _placeList = MutableLiveData<List<Place>>()
    val placeList: LiveData<List<Place>> = _placeList

    private val _currentVacationSpotList = MutableLiveData<List<RecommendPlace>>()
    val currentVacationSpotList: LiveData<List<RecommendPlace>> = _currentVacationSpotList

    private val _currentFoodList = MutableLiveData<List<RecommendPlace>>()
    val currentFoodList: LiveData<List<RecommendPlace>> = _currentFoodList

    private val _currentFestivalList = MutableLiveData<List<RecommendPlace>>()
    val currentFestivalList: LiveData<List<RecommendPlace>> = _currentFestivalList

    private val _currentPlace = MutableLiveData<Place>()
    val currentPlace: LiveData<Place> = _currentPlace

    private val _currentInd = MutableLiveData<Int>()
    val currentInd: LiveData<Int> = _currentInd

    var vacationPageInd = 0
    var foodPageInd = 0
    var festivalPageInd = 0

    private val _vacationPageEnd = MutableLiveData<Boolean>()
    val vacationPageEnd: LiveData<Boolean> = _vacationPageEnd

    private val _foodPageEnd = MutableLiveData<Boolean>()
    val foodPageEnd: LiveData<Boolean> = _foodPageEnd

    private val _festivalPageEnd = MutableLiveData<Boolean>()
    val festivalPageEnd: LiveData<Boolean> = _festivalPageEnd

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
            _vacationPageEnd.value = false
            _foodPageEnd.value = false
            _festivalPageEnd.value = false
            vacationPageInd = 0
            foodPageInd = 0
            festivalPageInd = 0
            val vacationRes = vacationSpotList.value?.get(ind)
            vacationRes?.let {
                _currentVacationSpotList.value = it
            }
            val foodRes = foodList.value?.get(ind)
            foodRes?.let {
                _currentFoodList.value = it
            }
            val festivalRes = festivalList.value?.get(ind)
            festivalRes?.let {
                _currentFestivalList.value = it
            }
        }
    }

    fun initVacationSpotData() {
        viewModelScope.launch {
            vacationPageInd++
            val vacationRes = mutableListOf<List<RecommendPlace>>()
            placeList.value?.forEach {
                val tempRes = withContext(Dispatchers.IO) {
                    CategoryMap.get(Category.VACATION)?.let { type ->
                        repository.loadAreaData(it.areaCode, type, vacationPageInd)
                    }
                }
                tempRes?.let {
                    vacationRes.add(it)
                }
            }
            _vacationSpotList.value = vacationRes
            val vacationTemp = vacationSpotList.value?.get(0)
            vacationTemp?.let {
                _currentVacationSpotList.value = it
            }
        }
    }

    fun initFoodData() {
        viewModelScope.launch {
            foodPageInd++
            val foodRes = mutableListOf<List<RecommendPlace>>()
            placeList.value?.forEach {
                val tempRes = withContext(Dispatchers.IO) {
                    CategoryMap.get(Category.FOOD)?.let { type ->
                        repository.loadAreaData(it.areaCode, type, foodPageInd)
                    }
                }
                tempRes?.let {
                    foodRes.add(it)
                }
            }
            _foodList.value = foodRes

            val foodTemp = foodList.value?.get(0)
            foodTemp?.let {
                _currentFoodList.value = it
            }
        }
    }

    fun initFestivalData() {
        viewModelScope.launch {
            festivalPageInd++
            val festivalRes = mutableListOf<List<RecommendPlace>>()
            placeList.value?.forEach {
                val tempRes = withContext(Dispatchers.IO) {
                    repository.loadFestivalData(it.areaCode, festivalPageInd)
                }
                festivalRes.add(tempRes)
            }
            _festivalList.value = festivalRes
            val festivalTemp = festivalList.value?.get(0)
            festivalTemp?.let {
                _currentFestivalList.value = it
            }
        }
    }
    fun addVacationData() {
        viewModelScope.launch {
            vacationPageInd++
            val currentRes = currentVacationSpotList.value?.toMutableList()
            val res = withContext(Dispatchers.IO) {
                currentPlace.value?.let {
                    CategoryMap.get(Category.VACATION)?.let { type ->
                        repository.loadAreaData(it.areaCode, type, vacationPageInd)
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
                _currentVacationSpotList.value = it
            }
        }
    }
    fun addFoodData() {
        viewModelScope.launch {
            foodPageInd++
            val currentRes = currentFoodList.value?.toMutableList()
            val res = withContext(Dispatchers.IO) {
                currentPlace.value?.let {
                    CategoryMap.get(Category.FOOD)?.let { type ->
                        repository.loadAreaData(it.areaCode, type, foodPageInd)
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
                _currentFoodList.value = it
            }
        }
    }
    fun addFestivalData() {
        viewModelScope.launch {
            festivalPageInd++
            val currentRes = currentFestivalList.value?.toMutableList()
            val res = withContext(Dispatchers.IO) {
                currentPlace.value?.let {
                    repository.loadFestivalData(it.areaCode, festivalPageInd)
                }
            }
            res?.forEach {
                currentRes?.add(it)
            }
            if (res?.size == 0) {
                _festivalPageEnd.value = true
            }
            currentRes?.let {
                _currentFestivalList.value = it
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
