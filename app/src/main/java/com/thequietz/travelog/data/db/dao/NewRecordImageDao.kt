package com.thequietz.travelog.data.db.dao

import androidx.room.Dao
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Transaction
import com.thequietz.travelog.ui.record.model.RecordImage

@Dao
abstract class NewRecordImageDao : BaseDao<NewRecordImage> {
    @Query("SELECT * FROM NewRecordImage")
    abstract fun loadAllNewRecordImages(): List<NewRecordImage>

    @Query("SELECT * FROM NewRecordImage WHERE newTravelId =:travelId")
    abstract fun loadNewRecordImagesByTravelId(travelId: Int): List<NewRecordImage>

    @Query("SELECT * FROM NewRecordImage WHERE newTravelId =:travelId AND newPlace =:place")
    abstract fun loadNewRecordImageByTravelIdAndDayAndPlace(
        travelId: Int,
        place: String
    ): List<NewRecordImage>

    @Query("SELECT * FROM NewRecordImage WHERE newRecordImageId =:id")
    abstract fun loadRecordImageById(id: Int): NewRecordImage

    @Query("SELECT * FROM NewRecordImage WHERE newTravelId =:travelId LIMIT :limit OFFSET :offset ")
    abstract fun loadFirstRowByTravelId(travelId: Int, limit: Int, offset: Int): NewRecordImage

    @Query("SELECT * FROM NewRecordImage WHERE newTravelId =:travelId GROUP BY newPlace ORDER BY isDefault")
    abstract fun loadAnyImageWithDistinctPlaceByTravelId(travelId: Int): List<NewRecordImage>

    @Query("UPDATE NewRecordImage SET comment =:comment WHERE newRecordImageId =:id")
    abstract fun updateRecordImageCommentById(comment: String, id: Int)

    @Query("DELETE FROM NewRecordImage WHERE newTravelId =:travelId AND isDefault =:isDefault")
    abstract fun deleteNewRecordImageByTravelIdAndIsDefault(travelId: Int, isDefault: Boolean)

    @Transaction
    open fun deleteAndInsertNewRecordImages(
        travelId: Int,
        isDefault: Boolean,
        tempNewRecordImages: List<NewRecordImage>
    ) {
        deleteNewRecordImageByTravelIdAndIsDefault(travelId, isDefault)
        insert(*tempNewRecordImages.toTypedArray())
    }
}

@Dao
abstract class JoinRecordDao : BaseDao<JoinRecord> {
    @Query(
        "SELECT * FROM (SELECT * FROM RecordImage, NewRecordImage) AS `Temp` WHERE `Temp`.travelId = `Temp`.newTravelId " +
            "AND `Temp`.place = `Temp`.newPlace AND `Temp`.travelId =:travelId "
    )
    abstract fun loadAllJoined(travelId: Int): List<JoinRecord>

    @Query(
        "SELECT * FROM (SELECT * FROM RecordImage, NewRecordImage) AS `Temp` WHERE `Temp`.travelId = `Temp`.newTravelId " +
            "AND `Temp`.place = `Temp`.newPlace AND `Temp`.travelId =:travelId " +
            "AND `Temp`.day =:day AND `Temp`.place =:place " +
            "AND `Temp`.isDefault !=:value LIMIT :limit OFFSET :offset"
    )
    abstract fun loadStartJoinedRecordByTravelIdAndDayAndPlace(
        travelId: Int,
        day: String,
        place: String,
        value: Boolean = true,
        limit: Int,
        offset: Int
    ): JoinRecord

    @Query(
        "SELECT * FROM (SELECT * FROM RecordImage, NewRecordImage) AS `Temp` WHERE `Temp`.travelId = `Temp`.newTravelId " +
            "AND `Temp`.place = `Temp`.newPlace AND `Temp`.travelId =:travelId " +
            "ORDER BY `Temp`.day"
    )
    abstract fun loadJoinedRecordByTravelIdIncludeDefault(travelId: Int): List<JoinRecord>

    @Query(
        "SELECT * FROM (SELECT * FROM RecordImage, NewRecordImage) AS `Temp` WHERE `Temp`.travelId = `Temp`.newTravelId " +
            "AND `Temp`.place = `Temp`.newPlace AND `Temp`.travelId =:travelId " +
            "AND `Temp`.isDefault !=:value ORDER BY `Temp`.day"
    )
    abstract fun loadJoinedRecordByTravelId(travelId: Int, value: Boolean = true): List<JoinRecord>

    @Query(
        "SELECT * FROM (SELECT * FROM RecordImage, NewRecordImage) AS `Temp` WHERE `Temp`.travelId = `Temp`.newTravelId " +
            "AND `Temp`.place = `Temp`.newPlace AND `Temp`.travelId =:travelId " +
            "AND `Temp`.place =:place AND `Temp`.isDefault =:value ORDER BY `Temp`.day"
    )
    abstract fun loadDefaultJoinedRecordByTravelId(
        travelId: Int,
        place: String,
        value: Boolean = true
    ): JoinRecord

    @Query(
        "SELECT * FROM (SELECT * FROM RecordImage, NewRecordImage) AS `Temp` WHERE `Temp`.travelId = `Temp`.newTravelId " +
            "AND `Temp`.place = `Temp`.newPlace AND `Temp`.newTravelId =:travelId " +
            "AND `Temp`.newPlace =:place AND `Temp`.isDefault !=:value ORDER BY `Temp`.day"
    )
    abstract fun loadJoinedRecordByTravelIdAndPlace(
        travelId: Int,
        place: String,
        value: Boolean = true
    ): List<JoinRecord>

    @Query(
        "SELECT * FROM (SELECT * FROM RecordImage, NewRecordImage) AS `Temp` WHERE `Temp`.travelId = `Temp`.newTravelId " +
            "AND `Temp`.place = `Temp`.newPlace AND `Temp`.newTravelId =:travelId " +
            "GROUP BY `Temp`.newPlace, `Temp`.day ORDER BY `Temp`.day ASC"
    )
    abstract fun loadAnyJoinedRecordByTravelIdAndPlace(travelId: Int): List<JoinRecord>

    @Query(
        "SELECT * FROM (SELECT * FROM RecordImage, NewRecordImage) AS `Temp` WHERE `Temp`.travelId = `Temp`.newTravelId " +
            "AND `Temp`.place = `Temp`.newPlace AND `Temp`.newTravelId =:travelId " +
            "GROUP BY `Temp`.place, `Temp`.day ORDER BY `Temp`.day ASC, `Temp`.isDefault ASC"
    )
    abstract fun loadDistinctPlaceAndDayFromJoinedRecordByTravelId(travelId: Int): List<JoinRecord>
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
