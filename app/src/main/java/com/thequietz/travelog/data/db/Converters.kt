package com.thequietz.travelog.data.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.thequietz.travelog.ui.place.model.PlaceDetailModel
import com.thequietz.travelog.ui.schedule.model.SchedulePlaceModel

class Converters {
    @TypeConverter
    fun stringListToJson(value: List<String>): String = Gson().toJson(value)

    @TypeConverter
    fun jsonToStringList(value: String): List<String> =
        Gson().fromJson(value, Array<String>::class.java).toList()

    @TypeConverter
    fun jsonToSchedulePlace(value: String): SchedulePlaceModel =
        Gson().fromJson(value, SchedulePlaceModel::class.java)

    @TypeConverter
    fun schedulePlaceToJson(value: SchedulePlaceModel): String = Gson().toJson(value)

    @TypeConverter
    fun placeListToJson(value: List<SchedulePlaceModel>): String = Gson().toJson(value)

    @TypeConverter
    fun jsonToPlaceList(value: String): List<SchedulePlaceModel> =
        Gson().fromJson(value, Array<SchedulePlaceModel>::class.java).toList()

    @TypeConverter
    fun placeDetailToJson(value: PlaceDetailModel): String = Gson().toJson(value)

    @TypeConverter
    fun jsonToPlaceDetail(value: String): PlaceDetailModel =
        Gson().fromJson(value, PlaceDetailModel::class.java)

    @TypeConverter
    fun placeDetailListToJson(value: List<PlaceDetailModel>): String = Gson().toJson(value)

    @TypeConverter
    fun jsonToPlaceDetailList(value: String): List<PlaceDetailModel> =
        Gson().fromJson(value, Array<PlaceDetailModel>::class.java).toList()
}
