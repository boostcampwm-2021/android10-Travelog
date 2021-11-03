package com.thequietz.travelog.schedule

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Schedule")
data class Schedule(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String = "",
    val place: List<String> = listOf(),
    val date: String = ""
)
