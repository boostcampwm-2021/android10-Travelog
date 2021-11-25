package com.thequietz.travelog.util

import com.thequietz.travelog.record.model.RecordImage
import com.thequietz.travelog.schedule.model.ScheduleModel
import com.thequietz.travelog.schedule.model.SchedulePlaceModel

enum class ScheduleControlType {
    TYPE_CREATE,
    TYPE_UPDATE
}

const val requestImage = 99
const val festivalContentTypeId = 999
val SAMPLE_SCHEDULES = listOf(
    ScheduleModel(
        0,
        "경주 여행",
        listOf(
            SchedulePlaceModel(
                thumbnail = "",
                areaCode = 0,
                mapX = 35.241615,
                mapY = 128.695587,
                "경주"
            )
        ),
        "2021.08.08 ~ 2021.08.10"
    ),
    ScheduleModel(
        0, "서울 여행",
        listOf(
            SchedulePlaceModel(
                thumbnail = "",
                areaCode = 0,
                mapX = 35.241615,
                mapY = 128.695587,
                "서울"
            ),
            SchedulePlaceModel(
                thumbnail = "",
                areaCode = 0,
                mapX = 35.241615,
                mapY = 128.695587,
                "경기"
            )
        ),
        "2021.10.05 ~ 2021.10.20"
    ),
    ScheduleModel(
        0, "강릉 여행",
        listOf(
            SchedulePlaceModel(
                thumbnail = "",
                areaCode = 0,
                mapX = 35.241615,
                mapY = 128.695587,
                "강릉"
            )
        ),
        "2021.12.24 ~ 2021.12.25"
    ),
)
val SAMPLE_RECORD_IMAGES = listOf(
    RecordImage().copy(
        travelId = 1,
        title = "제주도 여행",
        startDate = "2021.10.27",
        endDate = "2021.10.29",
        day = "Day1",
        place = "석굴암",
        url = "https://tong.visitkorea.or.kr/cms/resource/67/2558467_image2_1.jpg",
        comment = "comment11",
        group = 0
    ),
    RecordImage().copy(
        travelId = 1,
        title = "제주도 여행",
        startDate = "2021.10.27",
        endDate = "2021.10.29",
        day = "Day1",
        place = "석굴암",
        url = "https://tong.visitkorea.or.kr/cms/resource/21/2689521_image2_1.jpg",
        comment = "comment12",
        group = 0
    ),
    RecordImage().copy(
        travelId = 1,
        title = "제주도 여행",
        startDate = "2021.10.27",
        endDate = "2021.10.29",
        day = "Day1",
        place = "불국사",
        url = "https://tong.visitkorea.or.kr/cms/resource/53/1253553_image2_1.jpg",
        comment = "comment13",
        group = 1
    ),
    RecordImage().copy(
        travelId = 1,
        title = "제주도 여행",
        startDate = "2021.10.27",
        endDate = "2021.10.29",
        day = "Day1",
        place = "불국사",
        url = "http://tong.visitkorea.or.kr/cms/resource/22/2654222_image2_1.jpg",
        comment = "comment21",
        group = 1
    ),
    RecordImage().copy(
        travelId = 1,
        title = "제주도 여행",
        startDate = "2021.10.27",
        endDate = "2021.10.29",
        day = "Day2",
        place = "강릉",
        url = "http://tong.visitkorea.or.kr/cms/resource/56/2736256_image2_1.jpg",
        comment = "comment23",
        group = 2
    ),
    RecordImage().copy(
        travelId = 1,
        title = "제주도 여행",
        startDate = "2021.10.27",
        endDate = "2021.10.29",
        day = "Day2",
        place = "강릉",
        url = "http://tong.visitkorea.or.kr/cms/resource/54/644554_image2_1.jpg",
        comment = "comment22",
        group = 2
    ),
    RecordImage().copy(
        travelId = 1,
        title = "제주도 여행",
        startDate = "2021.10.27",
        endDate = "2021.10.29",
        day = "Day3",
        place = "제주도",
        url = "http://tong.visitkorea.or.kr/cms/resource/60/489560_image2_1.jpg",
        comment = "comment31",
        group = 3
    ),
    RecordImage().copy(
        title = "제주도 여행",
        startDate = "2021.10.27",
        endDate = "2021.10.29",
        day = "Day3",
        place = "한라산",
        url = "http://tong.visitkorea.or.kr/cms/resource/28/2735328_image2_1.png",
        comment = "comment32",
        group = 4
    ),
    RecordImage().copy(
        travelId = 1,
        title = "제주도 여행",
        startDate = "2021.10.27",
        endDate = "2021.10.29",
        day = "Day3",
        place = "혿대",
        url = "http://tong.visitkorea.or.kr/cms/resource/46/2628546_image2_1.jpg",
        comment = "comment33",
        group = 5
    )
)
val areaCodeList = hashMapOf<Int, String>(
    1 to "서울특별시",
    2 to "인천광역시",
    3 to "대전광역시",
    4 to "대구광역시",
    5 to "광주광역시",
    6 to "부산광역시",
    7 to "울산광역시",
    8 to "세종특별자치시",
    31 to "경기도",
    32 to "강원도",
    33 to "충청북도",
    34 to "충청남도",
    35 to "경상북도",
    36 to "경상남도",
    37 to "전라북도",
    38 to "전라남도",
    39 to "제주도",
)
