package com.example.repetodo.template

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.repetodo.database.*
import kotlinx.coroutines.*

class TemplateViewModel(val databaseDao: TemplateDao, application: Application) : AndroidViewModel(application) {

    private var viewModelJob = Job()

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private var _templateTaskList = MutableLiveData<List<TemplateItem>> ()
    val templateTaskList: LiveData<List<TemplateItem>>
        get() = _templateTaskList

    init {
        getTaskList()
    }

    private fun getTaskList() {
        uiScope.launch {
            _templateTaskList.value = getAllItems()
        }
    }

    private suspend fun getAllItems(): List<TemplateItem>? {
        return withContext(Dispatchers.IO) {
            lateinit var task: List<TemplateItem>
            task = databaseDao.getAllItems()
            task
        }
    }

    fun addNewTask(newTaskTitle: String) {
        uiScope.launch {
            var newItem = TemplateItem()
            newItem.templateItemTitle = newTaskTitle
            insert(newItem)
            getTaskList()
        }


        Log.i("TemplateViewModel", "Add a new task")
        Log.i("TemplateViewModel", _templateTaskList.value!!.joinToString())
    }

    private suspend fun insert(task: TemplateItem) {
        withContext(Dispatchers.IO) {
            databaseDao.insert(task)
        }
    }

    fun deleteTask(taskId: Long) {
        uiScope.launch {
            delete(taskId)
            getTaskList()
        }
        Log.i("TemplateViewModel","Delete $taskId")

//        _templateTaskList.value = _templateTaskList.value!!.subList(0, taskId) + _templateTaskList.value!!.subList(taskId+1, _templateTaskList.value!!.size)

    }

    private suspend fun delete(taskId: Long) {
        withContext(Dispatchers.IO) {
            databaseDao.delete(taskId)
        }
    }

    fun updateTask(taskId: Long, taskTitle: String) {
        Log.i("TemplateViewModel","update $taskId")
        uiScope.launch {
            var task = getTask(taskId)
            task!!.templateItemTitle = taskTitle
            update(task)
        }
    }

    private suspend fun getTask(taskId: Long): TemplateItem? {
        return withContext(Dispatchers.IO) {
            var task = databaseDao.get(taskId)
            task
        }
    }

    private suspend fun update(task: TemplateItem) {
        withContext(Dispatchers.IO) {
            databaseDao.update(task)
        }
    }

}