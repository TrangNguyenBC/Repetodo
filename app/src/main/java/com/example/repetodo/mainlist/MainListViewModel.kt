package com.example.repetodo.mainlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainListViewModel : ViewModel() {

    private var _taskList = MutableLiveData<MutableList<String>> ()
    val taskList: LiveData<MutableList<String>>
        get() = _taskList

    init {
        _taskList.value = mutableListOf<String>("Go to shopping", "Pick kids", "Write emails")
    }

    fun addNewTask(newTaskTitle: String) {
        _taskList.value!!.add(newTaskTitle)
    }
}