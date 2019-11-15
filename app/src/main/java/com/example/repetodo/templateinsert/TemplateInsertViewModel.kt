package com.example.repetodo.templateinsert

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.repetodo.database.*
import kotlinx.coroutines.*

class TemplateInsertViewModel(val templateListDao: TemplateListDao, val templateDao: TemplateDao, val taskListDao: TaskListDao,
                              application: Application) : AndroidViewModel(application) {
    private var viewModelJob = Job()

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private var templatesList : List<Template> = listOf ()

    private var _templateInsertList = MutableLiveData<List<TemplateInsert>> ()
    val templateInsertList: LiveData<List<TemplateInsert>>
        get() = _templateInsertList


    private var _finishInsertion = MutableLiveData<Boolean> ()
    val finishInsertion: LiveData<Boolean>
        get() = _finishInsertion

    init {
        _finishInsertion.value = false
        _templateInsertList.value = listOf<TemplateInsert>()
        getTemplateList()

    }

    private fun getTemplateList() {
        uiScope.launch {
            templatesList = getAllTemplates()
            Log.i("TemplateInsertViewModel", "xin chao ${templatesList.joinToString()}")

            for (template in templatesList) {
                var templateId = template.templateId
                var latestItems = getTemplatesLastestItems(templateId, 3)
                var templateBrief = latestItems.joinToString(", ")
                var aTemplate = TemplateInsert(templateId, template.templateTitle, templateBrief)
                Log.i("TemplateInsertViewModel", "this is a template brief: $templateBrief")
                _templateInsertList.value = _templateInsertList.value!!.plus(aTemplate)
            }
            Log.i("TemplateInsertViewModel", "Initiate the TemplateInsertViewModel ${_templateInsertList.value!!.joinToString()}")
        }
    }

    private suspend fun getAllTemplates(): List<Template> {
        return withContext(Dispatchers.IO) {
            lateinit var template: List<Template>
            template = templateListDao.getAllItems()
            template
        }
    }

    private suspend fun getTemplatesLastestItems(templateId: Long, number: Int): List<String> {
        return withContext(Dispatchers.IO) {
            var result = templateDao.getLastestTitles(templateId, number)
            result
        }
    }

    fun insertFromTemplate(templateId: Long) {
        uiScope.launch {
            Log.i("TemplateInsertViewModel", "finishInsertion: ${finishInsertion.value}")
                var templateItems = getAllTemplateItems(templateId)
            var itemList: MutableList<TaskInformation> = mutableListOf()
            for (anItem in templateItems) {
                var task = TaskInformation()
                task.taskStatus = 0
                task.taskTitle = anItem.templateItemTitle
                itemList.add(task)
            }
            val taskList = itemList.toList()
            insertAllItem(taskList)

            var newList = getAllTask()
            Log.i("TemplateInsertViewModel", "Finish insertion ${newList!!.joinToString()}")

            // after insertion
            _finishInsertion.value = true
            Log.i("TemplateInsertViewModel", "finishInsertion: ${finishInsertion.value}")

        }
    }

    private suspend fun getAllTask(): List<TaskInformation> {
        return withContext(Dispatchers.IO) {
            var result = taskListDao.getAllTasks()
            result
        }
    }

    private suspend fun getAllTemplateItems(templateId: Long): List<TemplateItem> {
        return withContext(Dispatchers.IO) {
            var result = templateDao.getItemWithTemplate(templateId)
            result
        }
    }

    private suspend fun insertAllItem(taskList: List<TaskInformation>) {
        withContext(Dispatchers.IO) {
            taskListDao.insertAll(*taskList.toTypedArray())
        }
    }

}