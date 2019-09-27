package com.example.repetodo.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "task_list_table")
data class TaskInformation(
    @PrimaryKey(autoGenerate = true)
    var taskId: Long = 0L,

    @ColumnInfo(name = "task_title")
    val taskTitle: String = "",

    @ColumnInfo(name = "quality_rating")
    var taskStatus: Int = 0
)
