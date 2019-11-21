package com.example.repetodo.mainlist

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.repetodo.Utils.TasksFilterType
import com.example.repetodo.database.TaskListDao

class MainListViewModelFactory(private val dataSource: TaskListDao,
                               private val application: Application,
                               private val currentFilter: TasksFilterType
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainListViewModel::class.java)) {
            return MainListViewModel(dataSource, application, currentFilter) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
