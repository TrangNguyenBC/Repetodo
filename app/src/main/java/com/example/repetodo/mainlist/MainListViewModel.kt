package com.example.repetodo.mainlist

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.repetodo.database.TaskDatabaseDao
import com.example.repetodo.database.TaskInformation
import kotlinx.coroutines.*

class MainListViewModel(val database: TaskDatabaseDao, application: Application) : AndroidViewModel(application) {

    private var viewModelJob = Job()

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)



    private var _taskList = MutableLiveData<List<TaskInformation>> ()
    val taskList: LiveData<List<TaskInformation>>
        get() = _taskList

    init {
        initializeTaskList()
    }

    private fun initializeTaskList() {
        uiScope.launch {
            _taskList.value = getAllTasks()
        }

    }
    private suspend fun getAllTasks(): List<TaskInformation>? {
        return withContext(Dispatchers.IO) {
            var task = database.getAllTasks()
            task
        }
    }

    fun addNewTask(newTaskTitle: String) {
        uiScope.launch {
            var newTask = TaskInformation()
            newTask.taskStatus = 0
            newTask.taskTitle = newTaskTitle
            insert(newTask)
            _taskList.value = getAllTasks()
        }


        Log.i("MainListViewModel", "Add a new task")
        Log.i("MainListViewModel", _taskList.value!!.joinToString())
    }

    private suspend fun insert(task: TaskInformation) {
        withContext(Dispatchers.IO) {
            database.insert(task)
        }
    }

    fun deleteTask(taskId: Int) {
        Log.i("MainListViewModel","Delete $taskId")

//        _taskList.value = _taskList.value!!.subList(0, taskId) + _taskList.value!!.subList(taskId+1, _taskList.value!!.size)

    }

//    fun editTask(taskId: Int, taskTitle: String) {
//        //modify the task
//    }
}