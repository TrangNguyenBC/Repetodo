package com.example.repetodo.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * A databaseDao that stores TaskInformation information.
 * And a global method to get access to the databaseDao.
 *
 * This pattern is pretty much the same for any databaseDao,
 * so you can reuse it.
 */
@Database(entities = [TaskInformation::class, TemplateItem::class, Template::class], version = 3, exportSchema = false)
abstract class TaskDatabase : RoomDatabase() {

    /**
     * Connects the databaseDao to the DAO.
     */
    abstract val taskListDao: TaskListDao
    abstract val templateDao: TemplateDao
    abstract val templateListDao: TemplateListDao

    /**
     * Define a companion object, this allows us to add functions on the TaskDatabase class.
     *
     * For example, clients can call `TaskDatabase.getInstance(context)` to instantiate
     * a new TaskDatabase.
     */
    companion object {
        /**
         * INSTANCE will keep a reference to any databaseDao returned via getInstance.
         *
         * This will help us avoid repeatedly initializing the databaseDao, which is expensive.
         *
         *  The value of a volatile variable will never be cached, and all writes and
         *  reads will be done to and from the main memory. It means that changes made by one
         *  thread to shared data are visible to other threads.
         */
        @Volatile
        private var INSTANCE: TaskDatabase? = null

        /**
         * Helper function to get the databaseDao.
         *
         * If a databaseDao has already been retrieved, the previous databaseDao will be returned.
         * Otherwise, create a new databaseDao.
         *
         * This function is threadsafe, and callers should cache the result for multiple databaseDao
         * calls to avoid overhead.
         *
         * This is an example of a simple Singleton pattern that takes another Singleton as an
         * argument in Kotlin.
         *
         * To learn more about Singleton read the wikipedia article:
         * https://en.wikipedia.org/wiki/Singleton_pattern
         *
         * @param context The application context Singleton, used to get access to the filesystem.
         */
        fun getInstance(context: Context): TaskDatabase {
            // Multiple threads can ask for the databaseDao at the same time, ensure we only initialize
            // it once by using synchronized. Only one thread may enter a synchronized block at a
            // time.
            synchronized(this) {
                // Copy the current value of INSTANCE to a local variable so Kotlin can smart cast.
                // Smart cast is only available to local variables.
                var instance = INSTANCE
                // If instance is `null` make a new databaseDao instance.
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        TaskDatabase::class.java,
                        "task_database"
                    )
                        // Wipes and rebuilds instead of migrating if no Migration object.
                        // Migration is not part of this lesson. You can learn more about
                        // migration with Room in this blog post:
                        // https://medium.com/androiddevelopers/understanding-migrations-with-room-f01e04b07929
                        .fallbackToDestructiveMigration()
                        .build()
                    // Assign INSTANCE to the newly created databaseDao.
                    INSTANCE = instance
                }
                // Return instance; smart cast to be non-null.
                return instance
            }
        }
    }
}
