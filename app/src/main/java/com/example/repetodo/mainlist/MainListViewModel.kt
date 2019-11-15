package com.example.repetodo.mainlist

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.repetodo.database.TaskListDao
import com.example.repetodo.database.TaskInformation
import kotlinx.coroutines.*

class MainListViewModel(val database: TaskListDao, application: Application) : AndroidViewModel(application) {

    private var viewModelJob = Job()

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private var _hideCompletedTasks = MutableLiveData<Boolean> ()
    val hideCompletedTasks: LiveData<Boolean>
        get() = _hideCompletedTasks

    private var _taskList = MutableLiveData<List<TaskInformation>> ()
    val taskList: LiveData<List<TaskInformation>>
        get() = _taskList

    init {
        _hideCompletedTasks.value = true
        getTaskList()
        Log.i("MainListViewModel", "Initiate MainListViewModel")
    }

    private fun getTaskList() {
        uiScope.launch {
            _taskList.value = getAllTasks()
            Log.i("MainListViewModel", _taskList.value!!.joinToString())
        }
    }

    private suspend fun getAllTasks(): List<TaskInformation>? {
        return withContext(Dispatchers.IO) {
            lateinit var task: List<TaskInformation>
            if (_hideCompletedTasks.value!!)
                task = database.getTasksWithStatus(0)
            else
                task = database.getAllTasks()

            task
        }
    }

    fun addNewTask(newTaskTitle: String) {
        uiScope.launch {
            var newTask = TaskInformation()
            newTask.taskStatus = 0
            newTask.taskTitle = newTaskTitle
            insert(newTask)
            getTaskList()
        }


        Log.i("MainListViewModel", "Add a new task")
        Log.i("MainListViewModel", _taskList.value!!.joinToString())
    }

    private suspend fun insert(task: TaskInformation) {
        withContext(Dispatchers.IO) {
            database.insert(task)
        }
    }

    fun deleteTask(taskId: Long) {
        uiScope.launch {
            delete(taskId)
            getTaskList()
        }
        Log.i("MainListViewModel","Delete $taskId")

//        _taskList.value = _taskList.value!!.subList(0, taskId) + _taskList.value!!.subList(taskId+1, _taskList.value!!.size)

    }

    private suspend fun delete(taskId: Long) {
        withContext(Dispatchers.IO) {
            database.delete(taskId)
        }
    }

    fun updateTask(taskId: Long, taskTitle: String) {
        Log.i("MainListViewModel","update $taskId")
        uiScope.launch {
            var task = getTask(taskId)
            task!!.taskTitle = taskTitle
            update(task)
        }
    }

    fun updateTaskStatus(taskId: Long, status: Boolean) {
        Log.i("MainListViewModel","update $taskId")
        uiScope.launch {
            var task = getTask(taskId)
            task!!.taskStatus = if (status == true) 1 else 0
            update(task)
        }
    }

    private suspend fun getTask(taskId: Long): TaskInformation? {
        return withContext(Dispatchers.IO) {
            var task = database.get(taskId)
            task
        }
    }

    private suspend fun update(task: TaskInformation) {
        withContext(Dispatchers.IO) {
            database.update(task)
        }
    }

    fun changeHideSetting() {
        _hideCompletedTasks.value = !_hideCompletedTasks.value!!
        getTaskList()
    }

    fun insertTemplate() {
        Log.i("MainListViewModel","Insert a list of task information")
        var taskString = listOf<String>("task 1", "task 2", "task 3")
        for (task in taskString) {
            addNewTask(task)
        }
    }

}