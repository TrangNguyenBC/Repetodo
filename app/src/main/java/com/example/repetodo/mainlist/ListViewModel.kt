package com.example.repetodo.mainlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ListViewModel : ViewModel() {
    private var _taskTitle = MutableLiveData<MutableList<String>> ()
    val taskTitle: MutableLiveData<MutableList<String>>
        get() = _taskTitle

    init {
        _taskTitle.value = mutableListOf<String>("Go to shopping", "Pick kids", "Write emails")
    }

    fun addTask(taskName: String) {
        _taskTitle.value!!.add(taskName)
    }
}