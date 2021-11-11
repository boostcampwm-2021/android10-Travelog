package com.thequietz.travelog.data.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.thequietz.travelog.schedule.model.PlaceModel

class Converters {
    @TypeConverter
    fun stringListToJson(value: List<String>): String = Gson().toJson(value)

    @TypeConverter
    fun jsonToStringList(value: String): List<String> =
        Gson().fromJson(value, Array<String>::class.java).toList()

    @TypeConverter
    fun placeListToJson(value: List<PlaceModel>): String = Gson().toJson(value)

    @TypeConverter
    fun jsonToPlaceList(value: String): List<PlaceModel> =
        Gson().fromJson(value, Array<PlaceModel>::class.java).toList()
}
