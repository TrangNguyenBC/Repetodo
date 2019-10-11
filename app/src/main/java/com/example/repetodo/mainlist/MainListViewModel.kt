package com.example.repetodo.mainlist

import android.util.Log
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
        Log.i("MainListViewModel", "Add a new task")
        Log.i("MainListViewModel", _taskList.value!!.joinToString())
    }


    fun deleteTask(taskId: Int) {

        _taskList.value = _taskList.value!!.subList(0, taskId) + _taskList.value!!.subList(taskId+1, _taskList.value!!.size)

    }

    fun editTask(taskId: Int, taskTitle: String) {
        //modify the task
    }
}