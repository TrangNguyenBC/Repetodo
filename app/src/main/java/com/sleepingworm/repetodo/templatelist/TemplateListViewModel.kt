package com.sleepingworm.repetodo.templatelist

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sleepingworm.repetodo.database.*
import kotlinx.coroutines.*

class TemplateListViewModel(val templateListDao: TemplateListDao, application: Application) : AndroidViewModel(application) {

    private var viewModelJob = Job()

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private var _templateList = MutableLiveData<List<Template>> ()
    val templateList: LiveData<List<Template>>
        get() = _templateList

    init {
        getTaskList()
    }

    private fun getTaskList() {
        uiScope.launch {
            _templateList.value = getAllItems()
        }
    }

    private suspend fun getAllItems(): List<Template>? {
        return withContext(Dispatchers.IO) {
            lateinit var template: List<Template>
            template = templateListDao.getAllItems()
            template
        }
    }

    fun addNewTemplate(newTempateTitle: String) {
        uiScope.launch {
            var newTemplate = Template()
            newTemplate.templateTitle = newTempateTitle
            insert(newTemplate)
            getTaskList()
        }


        Log.i("TemplateListViewModel", "Add a new template")
        Log.i("TemplateListViewModel", _templateList.value!!.joinToString())
    }

    private suspend fun insert(template: Template) {
        withContext(Dispatchers.IO) {
            templateListDao.insert(template)
        }
    }

    fun deleteTemplate(templateId: Long) {
        uiScope.launch {
            delete(templateId)
            getTaskList()
        }
        Log.i("TemplateListViewModel","Delete $templateId")

//        _templateList.value = _templateList.value!!.subList(0, taskId) + _templateList.value!!.subList(taskId+1, _templateList.value!!.size)

    }

    private suspend fun delete(templateId: Long) {
        withContext(Dispatchers.IO) {
            templateListDao.delete(templateId)
        }
    }

    fun updateTask(templateId: Long, templateTitle: String) {
        Log.i("TemplateViewModel","update $templateId")
        uiScope.launch {
            var template = getTask(templateId)
            template!!.templateTitle = templateTitle
            update(template)
        }
    }

    private suspend fun getTask(templateId: Long): Template?
    {
        return withContext(Dispatchers.IO) {
            var template = templateListDao.get(templateId)
            template
        }
    }

    private suspend fun update(template: Template) {
        withContext(Dispatchers.IO) {
            templateListDao.update(template)
        }
    }

}