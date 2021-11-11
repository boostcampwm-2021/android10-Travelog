package com.thequietz.travelog.guide.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thequietz.travelog.data.RepositoryImpl
import com.thequietz.travelog.guide.Place
import com.thequietz.travelog.guide.view.SpecificGuideFragmentArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SpecificGuideViewModel @Inject internal constructor(
    val repository: RepositoryImpl
) : ViewModel() {

    private val _currentPlaceList = MutableLiveData<List<Place>>()
    val currentPlaceList: LiveData<List<Place>> = _currentPlaceList

    private val _currentSearch = MutableLiveData<String>()
    val currentSearch: LiveData<String> = _currentSearch

    private val _noData = MutableLiveData<Boolean>()
    val noData: LiveData<Boolean> = _noData

    fun initCurrentArgs(args: SpecificGuideFragmentArgs) {
        _currentSearch.value = args.item
        initCurrentItem()
    }
    fun initCurrentItem() {
        viewModelScope.launch {
            val res = withContext(Dispatchers.IO) {
                repository.loadDoSiByKeyword(_currentSearch.value!!)
            }
            _currentPlaceList.value = res
            _noData.value = currentPlaceList.value?.size == 0
        }
    }
}
