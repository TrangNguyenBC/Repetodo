package com.example.repetodo.mainlist

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.repetodo.R
import com.example.repetodo.databinding.FragmentMainListBinding

class MainListFragment : Fragment() {
    private lateinit var binding: FragmentMainListBinding
    private var taskTitle  = mutableListOf<String>("Go to shopping", "Pick kids", "Write emails")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main_list, container, false)

        val taskListAdapter = ListViewAdapter(this.activity!!, taskTitle)
        binding.taskListView.adapter = taskListAdapter
        binding.addButton.setOnClickListener {
            Log.i("MainListFragment","Button Add is clicked")

//            taskTitle.plus("new task")
//            // taskTitle.plus(binding.newTaskEditText.text.toString())
//            Log.i("MainListFragment",taskTitle.joinToString())

        }

        return binding.root
    }
}