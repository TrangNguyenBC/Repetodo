package com.example.repetodo.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

/**
 * Defines methods for using the TaskInformation class with Room.
 */
@Dao
interface TaskDatabaseDao {

    @Insert
    fun insert(task: TaskInformation)

    /**
     * When updating a row with a value already set in a column,
     * replaces the old value with the new one.
     *
     * @param task new value to write
     */
    @Update
    fun update(task: TaskInformation)

    /**
     * Selects and returns the row that matches the supplied start time, which is our key.
     *
     * @param key startTimeMilli to match
     */
    @Query("SELECT * from task_list_table WHERE taskId = :key")
    fun get(key: Long): TaskInformation?

    /**
     * Deletes all values from the table.
     *
     * This does not delete the table, only its contents.
     */
    @Query("DELETE FROM task_list_table")
    fun clear()

    /**
     * Selects and returns all rows in the table,
     *
     * sorted by start time in descending order.
     */
    @Query("SELECT * FROM task_list_table ORDER BY taskId DESC")
    fun getAllTasks(): LiveData<List<TaskInformation>>

    /**
     * Selects and returns the latest task.
     */
    @Query("SELECT * FROM task_list_table ORDER BY taskId DESC LIMIT 1")
    fun getLastestTask(): TaskInformation?

}
