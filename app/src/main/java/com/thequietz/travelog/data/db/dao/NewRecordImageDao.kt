package com.thequietz.travelog.data.db.dao

import androidx.room.Dao
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Query
import com.thequietz.travelog.record.model.RecordImage

@Dao
abstract class NewRecordImageDao : BaseDao<NewRecordImage> {
    @Query("SELECT * FROM NewRecordImage WHERE newTravelId =:travelId")
    abstract fun loadNewRecordImagesByTravelId(travelId: Int): List<NewRecordImage>

    @Query("SELECT * FROM NewRecordImage WHERE newTravelId =:travelId AND newPlace =:place")
    abstract fun loadNewRecordImageByTravelIdAndDayAndPlace(travelId: Int, place: String): List<NewRecordImage>

    @Query("SELECT * FROM NewRecordImage WHERE newRecordImageId =:id")
    abstract fun loadRecordImageById(id: Int): NewRecordImage

    @Query("SELECT * FROM NewRecordImage WHERE newTravelId =:travelId LIMIT :limit OFFSET :offset ")
    abstract fun loadFirstRowByTravelId(travelId: Int, limit: Int, offset: Int): NewRecordImage

    @Query("SELECT * FROM NewRecordImage WHERE newTravelId =:travelId GROUP BY newPlace")
    abstract fun loadAnyImageWithDistinctPlaceAndScheduleByTravelId(travelId: Int): List<NewRecordImage>

    @Query("UPDATE NewRecordImage SET comment =:comment WHERE newRecordImageId =:id")
    abstract fun updateRecordImageCommentById(comment: String, id: Int)
}

@Dao
abstract class JoinRecordDao : BaseDao<JoinRecord> {
    @Query(
        "SELECT RecordImage.*, NewRecordImage.* FROM RecordImage " +
            "INNER JOIN NewRecordImage ON RecordImage.travelId = NewRecordImage.newTravelId " +
            "AND RecordImage.place = NewRecordImage.newPlace WHERE NewRecordImage.newTravelId =:travelId " +
            "AND NewRecordImage.isDefault !=:value ORDER BY RecordImage.day"
    )
    abstract fun loadJoinedRecordByTravelId(travelId: Int, value: Boolean = true): List<JoinRecord>

    @Query(
        "SELECT RecordImage.*, NewRecordImage.* FROM RecordImage " +
            "INNER JOIN NewRecordImage ON RecordImage.travelId = NewRecordImage.newTravelId " +
            "AND RecordImage.place = NewRecordImage.newPlace WHERE NewRecordImage.newTravelId =:travelId " +
            "AND NewRecordImage.newPlace =:place AND NewRecordImage.isDefault =:value ORDER BY RecordImage.day"
    )
    abstract fun loadDefaultJoinedRecordByTravelId(travelId: Int, place: String, value: Boolean = true): JoinRecord

    @Query(
        "SELECT RecordImage.*, NewRecordImage.* FROM RecordImage " +
            "INNER JOIN NewRecordImage ON RecordImage.travelId = NewRecordImage.newTravelId " +
            "AND RecordImage.place = NewRecordImage.newPlace WHERE NewRecordImage.newTravelId =:travelId " +
            "AND NewRecordImage.newPlace =:place AND NewRecordImage.isDefault !=:value ORDER BY RecordImage.day"
    )
    abstract fun loadJoinedRecordByTravelIdAndPlace(travelId: Int, place: String, value: Boolean = true): List<JoinRecord>
}

@Entity(tableName = "NewRecordImage")
data class NewRecordImage(
    @PrimaryKey(autoGenerate = true) val newRecordImageId: Int = 0,
    val newTravelId: Int = 1,
    val newTitle: String = "",
    val newPlace: String = "",
    val url: String = "",
    val comment: String = "",
    val isDefault: Boolean = false
)
@Entity(tableName = "JoinRecord")
data class JoinRecord(
    @PrimaryKey
    @Embedded
    val recordImage: RecordImage,
    @Embedded
    val newRecordImage: NewRecordImage
)
