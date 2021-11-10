package com.thequietz.travelog.record

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thequietz.travelog.data.RepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecordViewManyViewModel @Inject constructor(
    val repository: RepositoryImpl
) : ViewModel() {

    private val _dataList = MutableLiveData<List<MyRecord>>()
    val dataList: LiveData<List<MyRecord>> = _dataList

    init {
        viewModelScope.launch {
            with(Dispatchers.IO) {
                val list = mutableListOf<MyRecord>()
                list.add(
                    MyRecord.RecordSchedule().copy(
                        date = 1
                    )
                )
                list.add(
                    MyRecord.RecordPlace().copy(
                        place = listOf("석굴암, 울산바위")
                    )
                )
                list.add(
                    MyRecord.RecordImageList().copy(
                        list = mutableListOf(
                            RecordImage().copy(
                                img = "https://tong.visitkorea.or.kr/cms/resource/67/2558467_image2_1.jpg",
                                comment = "comment11",
                                group = 0
                            ),
                            RecordImage().copy(
                                img = "https://tong.visitkorea.or.kr/cms/resource/21/2689521_image2_1.jpg",
                                comment = "comment12",
                                group = 0
                            ),
                            RecordImage().copy(
                                img = "https://tong.visitkorea.or.kr/cms/resource/53/1253553_image2_1.jpg",
                                comment = "comment13",
                                group = 0
                            ),
                            RecordImage().copy(
                                img = "http://tong.visitkorea.or.kr/cms/resource/22/2654222_image2_1.jpg",
                                comment = "comment21",
                                group = 0
                            ),
                            RecordImage().copy(
                                img = "http://tong.visitkorea.or.kr/cms/resource/56/2736256_image2_1.jpg",
                                comment = "comment23",
                                group = 1
                            ),
                        )
                    )
                )
                list.add(
                    MyRecord.RecordSchedule().copy(
                        date = 2
                    )
                )
                list.add(
                    MyRecord.RecordPlace().copy(
                        place = listOf("제주도, 용오름")
                    )
                )
                list.add(
                    MyRecord.RecordImageList().copy(
                        list = mutableListOf(
                            RecordImage().copy(
                                img = "http://tong.visitkorea.or.kr/cms/resource/54/644554_image2_1.jpg",
                                comment = "comment22",
                                group = 1
                            )
                        )
                    )
                )
                list.add(
                    MyRecord.RecordSchedule().copy(
                        date = 3
                    )
                )
                list.add(
                    MyRecord.RecordPlace().copy(
                        place = listOf("서울, 홍대")
                    )
                )
                list.add(
                    MyRecord.RecordImageList().copy(
                        list = mutableListOf(
                            RecordImage().copy(
                                img = "http://tong.visitkorea.or.kr/cms/resource/60/489560_image2_1.jpg",
                                comment = "comment31",
                                group = 2
                            ),
                            RecordImage().copy(
                                img = "http://tong.visitkorea.or.kr/cms/resource/28/2735328_image2_1.png",
                                comment = "comment32",
                                group = 2
                            ),
                            RecordImage().copy(
                                img = "http://tong.visitkorea.or.kr/cms/resource/46/2628546_image2_1.jpg",
                                comment = "comment33",
                                group = 2
                            )
                        )
                    )
                )
                _dataList.value = list
            }
        }
    }
}
