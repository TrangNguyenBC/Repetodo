package com.example.repetodo.database

import androidx.room.*

/**
 * Defines methods for using the TaskInformation class with Room.
 */
@Dao
interface TaskListDao {

    @Insert
    fun insert(task: TaskInformation)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg task: TaskInformation)

    /**
     * When updating a row with a value already set in a column,
     * replaces the old value with the new one.
     *
     * @param task new value to write
     */
    @Update
    fun update(task: TaskInformation)

    /**
     * Delete from the table.
     *
     * @param key Long to match
     */
    @Query("DELETE FROM task_list_table WHERE taskId = :key")
    fun delete(key: Long)

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
     * Selects and returns all rows in the table
     */
    @Query("SELECT * FROM task_list_table ORDER BY taskId DESC")
    fun getAllTasks(): List<TaskInformation>

    /**
     * Selects and returns all rows in the table with specific value of status
     */
    @Query("SELECT * FROM task_list_table WHERE task_status = :status ORDER BY taskId DESC")
    fun getTasksWithStatus(status: Int): List<TaskInformation>

    /**
     * Selects and returns all rows in the table with specific value of status
     */
    @Query("SELECT * FROM task_list_table WHERE task_title = :title ORDER BY taskId DESC")
    fun getTasksWithTitle(title: String): List<TaskInformation>

    /**
     * Selects and returns the latest task.
     */
    @Query("SELECT * FROM task_list_table ORDER BY taskId DESC LIMIT 1")
    fun getLastestTask(): TaskInformation?

}

