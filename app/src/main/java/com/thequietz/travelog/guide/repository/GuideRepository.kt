package com.thequietz.travelog.guide.repository

import com.thequietz.travelog.BuildConfig
import com.thequietz.travelog.data.TravelAPI
import com.thequietz.travelog.guide.model.GuideModel
import com.thequietz.travelog.guide.model.GuideName
import retrofit2.Retrofit
import retrofit2.awaitResponse
import retrofit2.converter.gson.GsonConverterFactory

private val nameList = listOf(
    GuideName(1, "서울"),
    GuideName(2, "인천"),
    GuideName(3, "대전"),
    GuideName(4, "대구"),
    GuideName(5, "광주"),
    GuideName(6, "부산"),
    GuideName(7, "울산"),
    GuideName(8, "세종"),
    GuideName(31, "경기"),
    GuideName(32, "강원"),
    GuideName(33, "충북"),
    GuideName(34, "충남"),
    GuideName(35, "경북"),
    GuideName(36, "경남"),
    GuideName(37, "전북"),
    GuideName(38, "전남"),
    GuideName(39, "제주"),
)

class GuideRepository {
    private val baseUrl = "http://api.visitkorea.or.kr/openapi/service/rest/KorService/"
    private val retrofit =
        Retrofit
            .Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    private val guideService = retrofit.create(TravelAPI::class.java)

    private suspend fun loadArea(code: Int): GuideModel? {
        val call =
            guideService.loadAreaList(
                serviceKey = BuildConfig.TOUR_API_KEY,
                code = code.toString()
            )
        val response = call.awaitResponse()
        if (!response.isSuccessful || response.body() == null) {
            return null
        }
        val body = response.body()!!
        val items = body.response.body.items.item
        if (items.isEmpty()) return null
        return items[0]
    }

    suspend fun loadAreaList(): List<GuideModel> {
        val areaList = mutableListOf<GuideModel>()
        for ((i, it) in nameList.withIndex()) {
            if (i > 4) break

            val item = loadArea(it.areaCode) ?: continue
            item.areaName = it.areaName
            item.thumbnail = item.thumbnail.replace("http://", "https://")
            areaList.add(item)
        }
        return areaList
    }
}
