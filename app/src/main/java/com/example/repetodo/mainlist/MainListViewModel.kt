package com.example.repetodo.mainlist

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainListViewModel : ViewModel() {

    private var _taskList = MutableLiveData<List<TaskData>> ()
    val taskList: LiveData<List<TaskData>>
        get() = _taskList

    init {
        val task1 = TaskData("Go to shopping", 0)
        val task2 = TaskData("Pick kids", 0)
        val task3 = TaskData("Write emails", 0)
        _taskList.value = listOf<TaskData>(task1, task2, task3)
    }

    fun addNewTask(newTaskTitle: String) {
        val newtask = TaskData(newTaskTitle, 0)
        _taskList.value = _taskList.value!!.plus(newtask)
        Log.i("MainListViewModel", "Add a new task")
        Log.i("MainListViewModel", _taskList.value!!.joinToString())
    }


    fun deleteTask(taskId: Int) {

        _taskList.value = _taskList.value!!.subList(0, taskId) + _taskList.value!!.subList(taskId+1, _taskList.value!!.size)

    }

//    fun editTask(taskId: Int, taskTitle: String) {
//        //modify the task
//    }
}