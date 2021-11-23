package com.thequietz.travelog.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import com.thequietz.travelog.record.model.RecordImage

@Dao
abstract class RecordImageDao : BaseDao<RecordImage> {
    @Query("SELECT * FROM RecordImage")
    abstract fun loadAllRecordImages(): List<RecordImage>

    @Query("SELECT * FROM RecordImage WHERE travelId =:travelId ORDER BY schedule, `group`")
    abstract fun loadRecordImageByTravelId(travelId: Int): List<RecordImage>

    @Query("SELECT * FROM RecordImage WHERE id =:id")
    abstract fun loadRecordImageById(id: Int): RecordImage

    @Query("SELECT * FROM RecordImage WHERE travelId =:travelId AND schedule =:day ORDER BY `group` DESC LIMIT 1")
    abstract fun loadLastRecordImageByTravelIdAndDay(travelId: Int, day: String): RecordImage

    @Query("SELECT `group` FROM RecordImage WHERE travelId =:travelId ORDER BY `group` DESC LIMIT 1")
    abstract fun loadGroupFromRecordImageByTravelId(travelId: Int): Int

    @Query("UPDATE RecordImage SET comment =:comment WHERE id =:id")
    abstract fun updateRecordImageCommentById(comment: String, id: Int)

    @Query("DELETE FROM RecordImage WHERE place =:place")
    abstract fun deleteRecordImageByPlace(place: String)
}
