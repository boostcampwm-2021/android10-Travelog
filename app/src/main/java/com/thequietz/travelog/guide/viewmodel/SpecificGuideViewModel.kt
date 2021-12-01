package com.thequietz.travelog.guide.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.Navigation
import com.google.gson.Gson
import com.thequietz.travelog.data.GuideRepository
import com.thequietz.travelog.guide.Place
import com.thequietz.travelog.guide.view.SpecificGuideFragment
import com.thequietz.travelog.guide.view.SpecificGuideFragmentArgs
import com.thequietz.travelog.guide.view.SpecificGuideFragmentDirections
import com.thequietz.travelog.util.areaCodeList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SpecificGuideViewModel @Inject internal constructor(
    val guideRepository: GuideRepository
) : ViewModel() {
    companion object {
        var previousSearch = ""
        var flag = false
    }

    private val _currentPlaceList = MutableLiveData<List<Place>>()
    val currentPlaceList: LiveData<List<Place>> = _currentPlaceList

    private val _currentSearch = MutableLiveData<String>()
    val currentSearch: LiveData<String> = _currentSearch

    private val _noData = MutableLiveData<Boolean>()
    val noData: LiveData<Boolean> = _noData

    fun initCurrentItem(
        args: SpecificGuideFragmentArgs,
        frag: SpecificGuideFragment
    ) {
        viewModelScope.launch {
            var previousSearchChanged = false
            try {
                val code = args.item.toInt()
                val res = withContext(Dispatchers.IO) {
                    guideRepository.loadDoSiByCode(code.toString())
                }
                _currentPlaceList.value = res
                _currentSearch.value = areaCodeList.get(res.get(0).areaCode.toInt())
                if (previousSearch != res.get(0).areaCode.toString()) {
                    previousSearchChanged = true
                }
                previousSearch = res.get(0).areaCode.toString()
                _noData.value = currentPlaceList.value?.size == 0
            } catch (e: NumberFormatException) {
                val res = withContext(Dispatchers.IO) {
                    guideRepository.loadDoSiByKeyword(args.item)
                }
                _currentPlaceList.value = res
                _currentSearch.value = args.item
                if (previousSearch != args.item) {
                    previousSearchChanged = true
                }
                previousSearch = args.item
                _noData.value = currentPlaceList.value?.size == 0
            }
            if (args.from == "Click" && previousSearchChanged) {
                if (currentPlaceList.value?.size == 1) {
                    frag.view?.let {
                        val param = Gson().toJson(currentPlaceList.value?.get(0))
                        val action = SpecificGuideFragmentDirections
                            .actionDirectlySpecificGuideFragmentToOtherInfoFragment(param)
                        Navigation.findNavController(it).navigate(action)
                    }
                }
            }
        }
    }
}
