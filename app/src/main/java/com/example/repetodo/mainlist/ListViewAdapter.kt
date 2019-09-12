package com.example.repetodo.mainlist

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import com.example.repetodo.R

class ListViewAdapter(private val context: FragmentActivity, private val taskList: Array<String>)
    : ArrayAdapter<String>(context, R.layout.item_task_info, taskList) {

    override fun getView(taskIndex: Int, view: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView = inflater.inflate(R.layout.item_task_info, null, true)

        val titleText = rowView.findViewById(R.id.title) as TextView

        titleText.text = taskList[taskIndex]

        return rowView
    }
}