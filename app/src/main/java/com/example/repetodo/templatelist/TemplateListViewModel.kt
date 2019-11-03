package com.example.repetodo.templatelist

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.repetodo.database.*
import kotlinx.coroutines.*

class TemplateListViewModel(val databaseDao: TemplateListDao, application: Application) : AndroidViewModel(application) {

    private var viewModelJob = Job()

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private var _templateTaskList = MutableLiveData<List<Template>> ()
    val templateTaskList: LiveData<List<Template>>
        get() = _templateTaskList

    init {
        getTaskList()
    }

    private fun getTaskList() {
        uiScope.launch {
            _templateTaskList.value = getAllItems()
        }
    }

    private suspend fun getAllItems(): List<Template>? {
        return withContext(Dispatchers.IO) {
            lateinit var template: List<Template>
            template = databaseDao.getAllItems()
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
        Log.i("TemplateListViewModel", _templateTaskList.value!!.joinToString())
    }

    private suspend fun insert(template: Template) {
        withContext(Dispatchers.IO) {
            databaseDao.insert(template)
        }
    }

    fun deleteTemplate(templateId: Long) {
        uiScope.launch {
            delete(templateId)
            getTaskList()
        }
        Log.i("TemplateListViewModel","Delete $templateId")

//        _templateTaskList.value = _templateTaskList.value!!.subList(0, taskId) + _templateTaskList.value!!.subList(taskId+1, _templateTaskList.value!!.size)

    }

    private suspend fun delete(templateId: Long) {
        withContext(Dispatchers.IO) {
            databaseDao.delete(templateId)
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
            var template = databaseDao.get(templateId)
            template
        }
    }

    private suspend fun update(template: Template) {
        withContext(Dispatchers.IO) {
            databaseDao.update(template)
        }
    }

}