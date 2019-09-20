package com.example.repetodo.mainlist

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.repetodo.R
import com.example.repetodo.databinding.FragmentMainListBinding
import kotlinx.android.synthetic.main.fragment_main_list.*

class MainListFragment : Fragment() {
    private lateinit var binding: FragmentMainListBinding
    private lateinit var viewModel: ListViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main_list, container, false)
        viewModel = ViewModelProviders.of(this).get(ListViewModel::class.java)

        val taskListAdapter = ListViewAdapter(this.activity!!, viewModel.taskTitle.value!!)

        binding.taskListView.adapter = taskListAdapter
        binding.addButton.setOnClickListener {
            viewModel.addTask(binding.newTaskEditText.text.toString())
            taskListAdapter.notifyDataSetChanged()
        }

        return binding.root
    }
}