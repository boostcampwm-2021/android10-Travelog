package com.thequietz.travelog.guide.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thequietz.travelog.data.GuideRepository
import com.thequietz.travelog.data.RecordRepository
import com.thequietz.travelog.data.db.dao.NewRecordImage
import com.thequietz.travelog.guide.model.Guide
import com.thequietz.travelog.record.model.RecordImage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class GuideViewModel @Inject internal constructor(
    val guideRepository: GuideRepository,
    val recordRepository: RecordRepository
) : ViewModel() {

    private val _dataList = MutableLiveData<List<Guide>>()
    val dataList: LiveData<List<Guide>> = _dataList

    init {
        CoroutineScope(Dispatchers.IO).launch {
            guideRepository.loadAllPlaceData()
        }
        change2MyGuide()
    }

    private fun change2MyGuide() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val res = mutableListOf<Guide>()
                res.add(
                    Guide.Header().copy(
                        title = "추천 여행지역"
                    )
                )
                val recommendList = withContext(Dispatchers.IO) {
                    guideRepository.loadRecommendPlaceData()
                }
                res.add(
                    Guide.SpecificRecommend().copy(
                        specificRecommendList = recommendList.toMutableList()
                    )
                )
                res.add(
                    Guide.Header().copy(
                        title = "여행지역 전체 보기"
                    )
                )
                val allDoSiList = withContext(Dispatchers.IO) {
                    guideRepository.loadAllDoSi()
                }
                res.add(
                    Guide.Dosi().copy(
                        specificDOSIList = allDoSiList.toMutableList()
                    )
                )
                withContext(Dispatchers.Main) {
                    _dataList.value = res
                }

                /*val record = mutableListOf<RecordImage>()
                val newRecord = mutableListOf<NewRecordImage>()
                record.add(
                    RecordImage().copy(
                        id = 1,
                        travelId = 1,
                        title = "제주도여행",
                        startDate = "20211130",
                        endDate = "20211134",
                        day = "day1",
                        place = "place1",
                        lat = 0.0,
                        lng = 0.0
                    )
                )
                record.add(RecordImage().copy(
                    id = 2,
                    travelId = 1,
                    title = "제주도여행",
                    startDate = "20211130",
                    endDate = "20211134",
                    day = "day2",
                    place = "place2",
                    lat = 0.0,
                    lng = 0.0
                ))
                record.add(
                    RecordImage().copy(
                    id = 3,
                    travelId = 1,
                    title = "제주도여행",
                    startDate = "20211130",
                    endDate = "20211134",
                    day = "day3",
                    place = "place3",
                    lat = 0.0,
                    lng = 0.0
                ))
                record.add(RecordImage().copy(
                    id = 4,
                    travelId = 1,
                    title = "제주도여행",
                    startDate = "20211130",
                    endDate = "20211134",
                    day = "day4",
                    place = "place4",
                    lat = 0.0,
                    lng = 0.0
                ))
                record.add(RecordImage().copy(
                    id = 5,
                    travelId = 2,
                    title = "울릉도여행",
                    startDate = "20211135",
                    endDate = "20211138",
                    day = "day1",
                    place = "place1",
                    lat = 0.0,
                    lng = 0.0
                ))
                //////////////////////////////////////
                newRecord.add(NewRecordImage().copy(
                    newRecordImageId = 1,
                    newTravelId = 1,
                    newTitle = "제주도여행",
                    newPlace = "place1",
                    url = "url11",
                    comment = "comment11",
                    isDefault = false
                ))
                newRecord.add(NewRecordImage().copy(
                    newRecordImageId = 2,
                    newTravelId = 1,
                    newTitle = "제주도여행",
                    newPlace = "place1",
                    url = "url12",
                    comment = "comment12",
                    isDefault = false
                ))
                newRecord.add(NewRecordImage().copy(
                    newRecordImageId = 3,
                    newTravelId = 1,
                    newTitle = "제주도여행",
                    newPlace = "place1",
                    url = "url13",
                    comment = "comment13",
                    isDefault = false
                ))
                newRecord.add(NewRecordImage().copy(
                    newRecordImageId = 4,
                    newTravelId = 1,
                    newTitle = "제주도여행",
                    newPlace = "place2",
                    url = "url21",
                    comment = "comment21",
                    isDefault = false
                ))
                newRecord.add(NewRecordImage().copy(
                    newRecordImageId = 5,
                    newTravelId = 1,
                    newTitle = "제주도여행",
                    newPlace = "place2",
                    url = "url22",
                    comment = "comment22",
                    isDefault = false
                ))
                newRecord.add(NewRecordImage().copy(
                    newRecordImageId = 6,
                    newTravelId = 1,
                    newTitle = "제주도여행",
                    newPlace = "place3",
                    url = "url31",
                    comment = "comment13",
                    isDefault = false
                ))
                newRecord.add(NewRecordImage().copy(
                    newRecordImageId = 7,
                    newTravelId = 2,
                    newTitle = "울릉도여행",
                    newPlace = "place4",
                    url = "url1",
                    comment = "comment1",
                    isDefault = false
                ))
                newRecord.add(NewRecordImage().copy(
                    newRecordImageId = 8,
                    newTravelId = 1,
                    newTitle = "제주도여행",
                    newPlace = "place1",
                    url = "empty",
                    comment = "comment11",
                    isDefault = true
                ))
                newRecord.add(NewRecordImage().copy(
                    newRecordImageId = 9,
                    newTravelId = 1,
                    newTitle = "제주도여행",
                    newPlace = "place2",
                    url = "empty",
                    comment = "comment11",
                    isDefault = true
                ))
                newRecord.add(NewRecordImage().copy(
                    newRecordImageId = 10,
                    newTravelId = 1,
                    newTitle = "제주도여행",
                    newPlace = "place3",
                    url = "empty",
                    comment = "comment11",
                    isDefault = true
                ))
                newRecord.add(
                    NewRecordImage().copy(
                        newRecordImageId = 11,
                        newTravelId = 1,
                        newTitle = "제주도여행",
                        newPlace = "place4",
                        url = "empty",
                        comment = "comment11",
                        isDefault = true
                    ))
                withContext(Dispatchers.IO) {
                    recordRepository.insertRecordImages(record)
                    recordRepository.insertNewRecordImages(newRecord)
                    val recordList = withContext(Dispatchers.IO) {
                        recordRepository.loadRecordImagesByTravelId(1)
                    }
                    val newRecordList = withContext(Dispatchers.IO) {
                        recordRepository.loadNewRecordImagesByTravelId(1)
                    }
                    val t = withContext(Dispatchers.IO) {
                        recordRepository.loadAll(1)
                    }
                    println("t Size  ${t.size}  ${recordList.size}  ${newRecordList.size}")
                    t.forEach{
                        println(it.toString())
                    }
                }*/


            }
        }
    }
}
