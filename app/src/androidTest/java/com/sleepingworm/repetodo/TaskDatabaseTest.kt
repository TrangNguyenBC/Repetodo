package com.sleepingworm.repetodo

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.sleepingworm.repetodo.database.TaskDatabase
import com.sleepingworm.repetodo.database.TaskListDao
import com.sleepingworm.repetodo.database.TaskInformation
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

/**
 * This is not meant to be a full set of tests. For simplicity, most of your samples do not
 * include tests. However, when building the Room, it is helpful to make sure it works before
 * adding the UI.
 */

@RunWith(AndroidJUnit4::class)
class TaskDatabaseTest {

    private lateinit var taskListDao: TaskListDao
    private lateinit var db: TaskDatabase

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        // Using an in-memory templateListDao because the information stored here disappears when the
        // process is killed.
        db = Room.inMemoryDatabaseBuilder(context, TaskDatabase::class.java)
            // Allowing main thread queries, just for testing.
            .allowMainThreadQueries()
            .build()
        taskListDao = db.taskListDao
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertEmptyTitleTask() {
        val task = TaskInformation()
        taskListDao.insert(task)
        val lastestTask = taskListDao.getLastestTask()
        assertEquals(lastestTask?.taskStatus, 0)
    }

//    @Test
//    @Throws(Exception::class)
//    fun insertAndGetTask() {
//        val task = TaskInformation()
//        taskListDao.insert(task)
//        val lastestTask = taskListDao.getLastestTask()
//        assertEquals(lastestTask?.taskStatus, 0)
//    }
}

