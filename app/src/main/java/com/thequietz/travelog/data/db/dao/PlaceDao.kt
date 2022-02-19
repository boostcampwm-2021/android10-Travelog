package com.thequietz.travelog.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import com.thequietz.travelog.ui.guide.Place

@Dao
abstract class PlaceDao : BaseDao<Place> {
    @Query("SELECT * FROM PlaceDB")
    abstract fun loadAllPlace(): List<Place>

    @Query("SELECT * FROM PlaceDB WHERE areaCode = :areacode")
    abstract fun loadAllPlaceByAreaCode(areacode: String): List<Place>

    @Query("SELECT * FROM PlaceDB WHERE stateName != '' order by areaCode ASC")
    abstract fun loadAllDOSI(): List<Place>

    @Query("SELECT * FROM PlaceDB WHERE name like :str")
    abstract fun loadAllPlaceByKeyWord(str: String): List<Place>
}
