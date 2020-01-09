package com.sleepingworm.repetodo.templatelist

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sleepingworm.repetodo.database.TemplateListDao

class TemplateListViewModelFactory(private val dataSource: TemplateListDao,
                                   private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TemplateListViewModel::class.java)) {
            return TemplateListViewModel(dataSource, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
