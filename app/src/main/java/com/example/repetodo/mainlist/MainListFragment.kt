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
    var viewModel = ListViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main_list, container, false)


        val taskListAdapter = ListViewAdapter(this.activity!!, viewModel.taskTitle.value!!)
        binding.taskListView.adapter = taskListAdapter
        binding.addButton.setOnClickListener {
            Log.i("MainListFragment","Button Add is clicked")
            viewModel.addTask(binding.newTaskEditText.text.toString())
            Log.i("MainListFragment",viewModel.taskTitle.value!!.joinToString())
        }

        return binding.root
    }
}