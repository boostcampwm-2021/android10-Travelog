package com.thequietz.travelog.guide.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thequietz.travelog.data.GuideRepository
import com.thequietz.travelog.guide.Place
import com.thequietz.travelog.guide.view.SpecificGuideFragmentArgs
import com.thequietz.travelog.util.areaCodeList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.NumberFormatException
import javax.inject.Inject

@HiltViewModel
class SpecificGuideViewModel @Inject internal constructor(
    val guideRepository: GuideRepository
) : ViewModel() {
    companion object {
        var previousSearch = ""
    }

    private val _currentPlaceList = MutableLiveData<List<Place>>()
    val currentPlaceList: LiveData<List<Place>> = _currentPlaceList

    private val _currentSearch = MutableLiveData<String>()
    val currentSearch: LiveData<String> = _currentSearch

    private val _noData = MutableLiveData<Boolean>()
    val noData: LiveData<Boolean> = _noData

    fun initCurrentItem(args: SpecificGuideFragmentArgs) {
        viewModelScope.launch {
            try {
                val code = args.item.toInt()
                val res = withContext(Dispatchers.IO) {
                    guideRepository.loadDoSiByCode(code.toString())
                }
                _currentPlaceList.value = res
                _currentSearch.value = areaCodeList.get(res.get(0).areaCode.toInt())
                previousSearch = res.get(0).areaCode.toString()
                _noData.value = currentPlaceList.value?.size == 0
            } catch (e: NumberFormatException) {
                val res = withContext(Dispatchers.IO) {
                    guideRepository.loadDoSiByKeyword(args.item)
                }
                _currentPlaceList.value = res
                _currentSearch.value = args.item
                previousSearch = args.item
                _noData.value = currentPlaceList.value?.size == 0
            }
        }
    }
}
