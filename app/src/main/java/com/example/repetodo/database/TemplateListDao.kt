package com.example.repetodo.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

/**
 * Defines methods for using the Template class with Room.
 */
@Dao
interface TemplateListDao {

    @Insert
    fun insert(template: Template)

    /**
     * When updating a row with a value already set in a column,
     * replaces the old value with the new one.
     *
     * @param template new value to write
     */
    @Update
    fun update(template: Template)

    /**
     * Delete from the table.
     *
     * @param key Long to match
     */
    @Query("DELETE FROM template_list_table WHERE templateId = :key")
    fun delete(key: Long)

    /**
     * Selects and returns the row that matches the supplied start time, which is our key.
     *
     * @param key startTimeMilli to match
     */
    @Query("SELECT * from template_list_table WHERE templateId = :key")
    fun get(key: Long): Template?

    /**
     * Deletes all values from the table.
     *
     * This does not delete the table, only its contents.
     */
    @Query("DELETE FROM template_list_table")
    fun clear()

    /**
     * Selects and returns all rows in the table
     */
    @Query("SELECT * FROM template_list_table ORDER BY templateId DESC")
    fun getAllItems(): List<Template>

    /**
     * Selects and returns all rows in the table with specific value of status
     */
    @Query("SELECT * FROM template_list_table WHERE template_title = :title ORDER BY templateId DESC")
    fun getTasksWithTitle(title: String): List<Template>

    /**
     * Selects and returns the latest task.
     */
    @Query("SELECT * FROM template_list_table ORDER BY templateId DESC LIMIT 1")
    fun getLastestItem(): Template?

}

