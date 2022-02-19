package com.thequietz.travelog.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.thequietz.travelog.ui.record.model.PlaceAndSchedule
import com.thequietz.travelog.ui.record.model.RecordImage

@Dao
abstract class RecordImageDao : BaseDao<RecordImage> {
    @Query("SELECT * FROM RecordImage")
    abstract fun loadAllRecordImages(): List<RecordImage>

    @Query("SELECT * FROM RecordImage WHERE travelId =:travelId ORDER BY day")
    abstract fun loadRecordImageByTravelId(travelId: Int): List<RecordImage>

    @Query("SELECT * FROM RecordImage WHERE id =:id")
    abstract fun loadRecordImageById(id: Int): RecordImage

//    @Query("SELECT * FROM RecordImage WHERE travelId =:travelId AND day =:day ORDER BY `group` DESC LIMIT 1")
//    abstract fun loadLastRecordImageByTravelIdAndDay(travelId: Int, day: String): RecordImage
//
//    @Query("SELECT `group` FROM RecordImage WHERE travelId =:travelId ORDER BY `group` DESC LIMIT 1")
//    abstract fun loadGroupFromRecordImageByTravelId(travelId: Int): Int

    @Query("SELECT DISTINCT `place`,`day` FROM RecordImage WHERE travelId =:travelId ORDER BY `day` ASC")
    abstract fun loadDistinctPlaceAndScheduleByTravelId(travelId: Int): List<PlaceAndSchedule>

//    @Query("SELECT DISTINCT `group` FROM RecordImage WHERE travelId =:travelId")
//    abstract fun loadDistinctGroupIdByTravelId(travelId: Int): List<Int>

    @Query("SELECT * FROM RecordImage WHERE travelId =:travelId LIMIT :limit OFFSET :offset ")
    abstract fun loadFirstRowByTravelId(travelId: Int, limit: Int, offset: Int): RecordImage

    @Query("SELECT * FROM RecordImage WHERE travelId =:travelId GROUP BY `place`, `day` ORDER BY `day`")
    abstract fun loadAnyImageWithDistinctPlaceAndScheduleByTravelId(travelId: Int): List<RecordImage>

//    @Query("UPDATE RecordImage SET comment =:comment WHERE id =:id")
//    abstract fun updateRecordImageCommentById(comment: String, id: Int)

    @Query("DELETE FROM RecordImage WHERE id =:id")
    abstract fun deleteRecordImageById(id: Int)

    @Query("DELETE FROM RecordImage WHERE place =:place")
    abstract fun deleteRecordImageByPlace(place: String)

    @Query("DELETE FROM RecordImage WHERE travelId =:travelId")
    abstract fun deleteRecordImageByTravelId(travelId: Int)

    @Transaction
    open fun deleteAndInsertRecordImages(travelId: Int, recordImages: List<RecordImage>) {
        deleteRecordImageByTravelId(travelId)
        insert(*recordImages.toTypedArray())
    }
}
