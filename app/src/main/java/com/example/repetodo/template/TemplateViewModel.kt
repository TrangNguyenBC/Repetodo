package com.example.repetodo.template

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.repetodo.database.*
import kotlinx.coroutines.*

class TemplateViewModel(val databaseDao: TemplateDao, application: Application, val templateId: Long) : AndroidViewModel(application) {

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
        getItemList()
    }

    private fun getItemList() {
        uiScope.launch {
            _templateTaskList.value = getAllItemsOfTemplate()
        }
    }

    private suspend fun getAllItemsOfTemplate(): List<TemplateItem>? {
        return withContext(Dispatchers.IO) {
            lateinit var task: List<TemplateItem>
            task = databaseDao.getItemWithTemplate(templateId)
            task
        }
    }

    fun addNewTask(newTaskTitle: String) {
        uiScope.launch {
            var newItem = TemplateItem()
            newItem.templateItemTitle = newTaskTitle
            newItem.templateId = templateId
            insert(newItem)
            getItemList()
        }


        Log.i("TemplateViewModel", "Add a new task")
        Log.i("TemplateViewModel", _templateTaskList.value!!.joinToString())
    }

    private suspend fun insert(task: TemplateItem) {
        withContext(Dispatchers.IO) {
            databaseDao.insert(task)
        }
    }

    fun deleteItem(itemId: Long) {
        uiScope.launch {
            delete(itemId)
            getItemList()
        }
        Log.i("TemplateViewModel","Delete $itemId")

//        _templateTaskList.value = _templateTaskList.value!!.subList(0, itemId) + _templateTaskList.value!!.subList(itemId+1, _templateTaskList.value!!.size)

    }

    private suspend fun delete(itemId: Long) {
        withContext(Dispatchers.IO) {
            databaseDao.delete(itemId)
        }
    }

    fun updateItem(itemId: Long, itemTitle: String) {
        Log.i("TemplateViewModel","update $itemId")
        uiScope.launch {
            var task = getItem(itemId)
            task!!.templateItemTitle = itemTitle
            update(task)
        }
    }

    private suspend fun getItem(itemId: Long): TemplateItem? {
        return withContext(Dispatchers.IO) {
            var task = databaseDao.get(itemId)
            task
        }
    }

    private suspend fun update(item: TemplateItem) {
        withContext(Dispatchers.IO) {
            databaseDao.update(item)
        }
    }

}