package com.thequietz.travelog.util

import com.thequietz.travelog.record.RecordImage
import com.thequietz.travelog.schedule.model.ScheduleModel

val SAMPLE_SCHEDULES = listOf(
    ScheduleModel(0, "경주 여행", listOf("경주"), "2021.08.08 ~ 2021.08.10"),
    ScheduleModel(0, "서울 여행", listOf("서울, 경기"), "2021.10.05 ~ 2021.10.20"),
    ScheduleModel(0, "강릉 여행", listOf("강릉"), "2021.12.24 ~ 2021.12.25"),
)
val SAMPLE_RECORD_IMAGES = listOf(
    RecordImage().copy(
        schedule = "Day1",
        place = "석굴암, 불국사",
        img = "https://tong.visitkorea.or.kr/cms/resource/67/2558467_image2_1.jpg",
        comment = "comment11",
        group = 0,
        id = 0
    ),
    RecordImage().copy(
        img = "https://tong.visitkorea.or.kr/cms/resource/21/2689521_image2_1.jpg",
        comment = "comment12",
        group = 0,
        id = 1
    ),
    RecordImage().copy(
        img = "https://tong.visitkorea.or.kr/cms/resource/53/1253553_image2_1.jpg",
        comment = "comment13",
        group = 0,
        id = 2
    ),
    RecordImage().copy(
        img = "http://tong.visitkorea.or.kr/cms/resource/22/2654222_image2_1.jpg",
        comment = "comment21",
        group = 0,
        id = 3
    ),
    RecordImage().copy(
        img = "http://tong.visitkorea.or.kr/cms/resource/56/2736256_image2_1.jpg",
        comment = "comment23",
        group = 0,
        id = 4
    ),
    RecordImage().copy(
        img = "http://tong.visitkorea.or.kr/cms/resource/54/644554_image2_1.jpg",
        comment = "comment22",
        group = 1,
        id = 5
    ),
    RecordImage().copy(
        img = "http://tong.visitkorea.or.kr/cms/resource/60/489560_image2_1.jpg",
        comment = "comment31",
        group = 2,
        id = 6
    ),
    RecordImage().copy(
        img = "http://tong.visitkorea.or.kr/cms/resource/28/2735328_image2_1.png",
        comment = "comment32",
        group = 2,
        id = 7
    ),
    RecordImage().copy(
        img = "http://tong.visitkorea.or.kr/cms/resource/46/2628546_image2_1.jpg",
        comment = "comment33",
        group = 2,
        id = 8
    )
)
