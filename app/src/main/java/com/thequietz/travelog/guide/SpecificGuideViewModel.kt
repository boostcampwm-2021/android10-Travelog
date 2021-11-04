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
class SpecificGuideViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val _allSpecificPlaceList = MutableLiveData<List<Place>>()
    val allSpecificPlaceList: LiveData<List<Place>> = _allSpecificPlaceList

    private val _currentPlace = MutableLiveData<Place>()
    val currentPlace: LiveData<Place> = _currentPlace

    fun initAllSpecificPlaceData() {
        viewModelScope.launch {
            val res = withContext(Dispatchers.IO) {
                repository.loadDoSiByCode(currentPlace.value!!.areaCode)
            }
            _allSpecificPlaceList.value = res
        }
    }

    fun initCurrentItem(item: Place) {
        _currentPlace.value = item
    }
}
