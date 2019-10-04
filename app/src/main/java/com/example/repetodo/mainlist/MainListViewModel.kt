package com.example.repetodo.mainlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainListViewModel : ViewModel() {

    private var _taskList = MutableLiveData<List<String>> ()
    val taskList: LiveData<List<String>>
        get() = _taskList

    init {
        _taskList.value = listOf<String>("Go to shopping", "Pick kids", "Write emails")
    }

    fun addNewTask(newTaskTitle: String) {
        _taskList.value = _taskList.value!!.plus(newTaskTitle)
    }

    fun deleteTask(taskId: Int) {
        // delete task
        _taskList.value = _taskList.value!!.drop(taskId)
    }

    fun editTask(taskId: Int, taskTitle: String) {
        //modify the task
    }
}