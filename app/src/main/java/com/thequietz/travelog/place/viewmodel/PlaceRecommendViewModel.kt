package com.thequietz.travelog.place.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.thequietz.travelog.BaseViewModel
import com.thequietz.travelog.place.model.PlaceRecommendModel
import com.thequietz.travelog.place.model.PlaceRecommendWithList
import com.thequietz.travelog.place.repository.PlaceRecommendRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlaceRecommendViewModel @Inject constructor(
    private val repository: PlaceRecommendRepository
) : BaseViewModel() {
    private var _landmarkData = MutableLiveData<List<PlaceRecommendModel>>()
    val landmarkData: LiveData<List<PlaceRecommendModel>> = _landmarkData

    private var _cultureData = MutableLiveData<List<PlaceRecommendModel>>()
    val cultureData: LiveData<List<PlaceRecommendModel>> = _cultureData

    private var _festivalData = MutableLiveData<List<PlaceRecommendModel>>()
    val festivalData: LiveData<List<PlaceRecommendModel>> = _festivalData

    private var _sportData = MutableLiveData<List<PlaceRecommendModel>>()
    val sportData: LiveData<List<PlaceRecommendModel>> = _sportData

    private var _resortData = MutableLiveData<List<PlaceRecommendModel>>()
    val resortData: LiveData<List<PlaceRecommendModel>> = _resortData

    private var _shoppingData = MutableLiveData<List<PlaceRecommendModel>>()
    val shoppingData: LiveData<List<PlaceRecommendModel>> = _shoppingData

    private var _restaurantData = MutableLiveData<List<PlaceRecommendModel>>()
    val restaurantData: LiveData<List<PlaceRecommendModel>> = _restaurantData

    private var _dataList = MutableLiveData<List<PlaceRecommendWithList>>()
    val dataList: LiveData<List<PlaceRecommendWithList>> = _dataList

    fun loadData() {
        viewModelScope.launch(mainDispatchers) {
            val deferJobs = listOf(
                async { _landmarkData.value = repository.loadPlaceData("?????????") },
                async { _cultureData.value = repository.loadPlaceData("????????????") },
                async { _festivalData.value = repository.loadPlaceData("?????? ??? ??????") },
                async { _sportData.value = repository.loadPlaceData("?????????") },
                async { _resortData.value = repository.loadPlaceData("????????????") },
                async { _shoppingData.value = repository.loadPlaceData("??????") },
                async { _restaurantData.value = repository.loadPlaceData("?????????") },
            )
            deferJobs.awaitAll()
            Log.d("STATUS", "HTTP RESPONSE")
            _dataList.value = listOf(
                PlaceRecommendWithList("?????????", landmarkData),
                PlaceRecommendWithList("????????????", cultureData),
                PlaceRecommendWithList("?????? ??? ??????", festivalData),
                PlaceRecommendWithList("?????????", sportData),
                PlaceRecommendWithList("????????????", resortData),
                PlaceRecommendWithList("??????", shoppingData),
                PlaceRecommendWithList("?????????", restaurantData),
            )
        }
    }

    override fun onCleared() {
        super.onCleared()

        Log.d("STATUS", "DATA REMOVED")
    }
}
