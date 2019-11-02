package com.example.repetodo.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

/**
 * Defines methods for using the TaskInformation class with Room.
 */
@Dao
interface TemplateDao {

    @Insert
    fun insert(templateItem: TemplateItem)

    /**
     * When updating a row with a value already set in a column,
     * replaces the old value with the new one.
     *
     * @param templateItem new value to write
     */
    @Update
    fun update(templateItem: TemplateItem)

    /**
     * Delete from the table.
     *
     * @param key Long to match
     */
    @Query("DELETE FROM template_table WHERE templateItemId = :key")
    fun delete(key: Long)

    /**
     * Selects and returns the row that matches the supplied start time, which is our key.
     *
     * @param key startTimeMilli to match
     */
    @Query("SELECT * from template_table WHERE templateItemId = :key")
    fun get(key: Long): TemplateItem?

    /**
     * Deletes all values from the table.
     *
     * This does not delete the table, only its contents.
     */
    @Query("DELETE FROM template_table")
    fun clear()

    /**
     * Selects and returns all rows in the table
     */
    @Query("SELECT * FROM template_table ORDER BY templateItemId DESC")
    fun getAllItems(): List<TemplateItem>

    /**
     * Selects and returns all rows in the table with specific value of status
     */
    @Query("SELECT * FROM template_table WHERE template_id = :templateId ORDER BY templateItemId DESC")
    fun getItemWithTemplate(templateId: Long): List<TemplateItem>

    /**
     * Selects and returns all rows in the table with specific value of status
     */
    @Query("SELECT * FROM template_table WHERE template_item_title = :title ORDER BY templateItemId DESC")
    fun getTasksWithTitle(title: String): List<TemplateItem>

    /**
     * Selects and returns the latest task.
     */
    @Query("SELECT * FROM template_table ORDER BY templateItemId DESC LIMIT 1")
    fun getLastestItem(): TemplateItem?

}

