package com.thequietz.travelog.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import com.thequietz.travelog.guide.RecommendPlace

@Dao
abstract class RecommendPlaceDao : BaseDao<RecommendPlace> {
    @Query("SELECT * FROM RecommendPlaceDB")
    abstract fun loadAllRecommendPlace(): List<RecommendPlace>

    @Query("SELECT * FROM RecommendPlaceDB WHERE areaCode= :areacode and sigunguCode= :sigungucode")
    abstract fun loadRecommendPlaceByAreaCodeAndSigunguCode(areacode: String = "1", sigungucode: String = "10"): List<RecommendPlace>

    @Query("SELECT * FROM RecommendPlaceDB WHERE areaCode= :areacode and category= :cat")
    abstract fun loadRecommendPlaceByAreaCodeAndCategory(areacode: String = "1", cat: String = "A01"): List<RecommendPlace>

    @Query("SELECT * FROM RecommendPlaceDB WHERE areaCode= :areacode and contentTypeId= :type")
    abstract fun loadRecommendFestivalByAreaCode(areacode: String, type: Int): List<RecommendPlace>
}
