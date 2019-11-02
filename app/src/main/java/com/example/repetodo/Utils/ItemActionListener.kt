package com.example.repetodo.Utils

interface ItemActionListener {
    fun onItemDelete(id: Long)
    fun onItemUpdate(id: Long, title: String)
    fun onItemCheckUpdate(id: Long, checked: Boolean)
    fun hideSoftKeyboard()
}