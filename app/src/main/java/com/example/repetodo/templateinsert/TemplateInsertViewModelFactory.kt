package com.example.repetodo.templateinsert

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.repetodo.database.TaskListDao
import com.example.repetodo.database.TemplateDao
import com.example.repetodo.database.TemplateListDao

class TemplateInsertViewModelFactory(private val dataSource: TemplateListDao,
                                     private val templateDao: TemplateDao,
                                     private val taskListDao: TaskListDao,
                                     private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TemplateInsertViewModel::class.java)) {
            return TemplateInsertViewModel(dataSource, templateDao, taskListDao, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
