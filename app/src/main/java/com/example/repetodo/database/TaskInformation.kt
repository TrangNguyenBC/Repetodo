package com.example.repetodo.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "task_list_table")
data class TaskInformation(
    @PrimaryKey(autoGenerate = true)
    var taskId: Long = 0L,

    @ColumnInfo(name = "task_title")
    var taskTitle: String = "",

    @ColumnInfo(name = "task_status")
    var taskStatus: Int = 0
)
