package com.sleepingworm.repetodo.database

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


@Entity(tableName = "template_list_table")
data class Template(
    @PrimaryKey(autoGenerate = true)
    var templateId: Long = 0L,

    @ColumnInfo(name = "template_title")
    var templateTitle: String = ""
)

@Entity(tableName = "template_table")
data class TemplateItem(
    @PrimaryKey(autoGenerate = true)
    var templateItemId: Long = 0L,

    @ColumnInfo(name = "template_id")
    var templateId: Long = 0L,

    @ColumnInfo(name = "template_item_title")
    var templateItemTitle: String = ""
)