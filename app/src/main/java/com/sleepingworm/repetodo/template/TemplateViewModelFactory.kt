package com.sleepingworm.repetodo.template

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sleepingworm.repetodo.database.TemplateDao

class TemplateViewModelFactory(private val dataSource: TemplateDao,
                               private val application: Application,
                               private val templateId: Long
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TemplateViewModel::class.java)) {
            return TemplateViewModel(dataSource, application, templateId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
