package com.thequietz.travelog.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.thequietz.travelog.schedule.Schedule

@Database(entities = [Schedule::class], version = 1)
abstract class AppDatabase : RoomDatabase()
