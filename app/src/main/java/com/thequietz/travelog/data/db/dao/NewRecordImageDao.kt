package com.thequietz.travelog.data.db.dao

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Query

@Dao
abstract class NewRecordImageDao : BaseDao<NewRecordImage> {
    @Query("SELECT * FROM NewRecordImage WHERE travelId =:travelId, day =:day, place =:place")
    abstract fun loadNewRecordImageByTravelIdAndDayAndAPlace(travelId: Int, day: String, place: String): List<NewRecordImage>

    @Query("SELECT * FROM NewRecordImage WHERE id =:id")
    abstract fun loadRecordImageById(id: Int): NewRecordImage

    @Query("SELECT * FROM NewRecordImage WHERE travelId =:travelId LIMIT :limit OFFSET :offset ")
    abstract fun loadFirstRowByTravelId(travelId: Int, limit: Int, offset: Int): NewRecordImage

    @Query("SELECT * FROM NewRecordImage WHERE travelId =:travelId GROUP BY `place`, `day` ORDER BY `day`")
    abstract fun loadAnyImageWithDistinctPlaceAndScheduleByTravelId(travelId: Int): List<NewRecordImage>

    @Query("UPDATE NewRecordImage SET comment =:comment WHERE id =:id")
    abstract fun updateRecordImageCommentById(comment: String, id: Int)
}

@Entity(tableName = "NewRecordImage")
data class NewRecordImage(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val travelId: Int = 1,
    val title: String = "",
    val day: String = "",
    val place: String = "",
    val url: String = "",
    val comment: String = "",
)
