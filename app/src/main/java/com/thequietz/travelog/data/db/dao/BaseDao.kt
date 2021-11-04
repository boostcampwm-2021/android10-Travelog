package com.thequietz.travelog.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert

@Dao
interface BaseDao<T> {
    @Insert
    fun insert(vararg obj: T)

    @Delete
    fun delete(obj: T)
}
