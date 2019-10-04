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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.repetodo.R
import com.example.repetodo.databinding.FragmentMainListBinding
import kotlinx.android.synthetic.main.fragment_task_item.view.*

class MainListFragment : Fragment() {
    private lateinit var binding: FragmentMainListBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var viewModel: MainListViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Use ViewModelProviders instead of construct view Model
        viewModel = ViewModelProviders.of(this).get(MainListViewModel::class.java)

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main_list, container, false)
        binding.viewModel = viewModel

        viewManager = LinearLayoutManager(this.context)
        var viewAdapter = MainTaskRecyclerAdapter()
        binding.taskRecyclerView.adapter = viewAdapter

        // update data set inside the adapter
        viewModel.taskList.observe(viewLifecycleOwner, Observer {
            it?.let {
                viewAdapter.myDataset = it
            }
        })

        recyclerView = binding.taskRecyclerView.apply {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            setHasFixedSize(true)

            // use a linear layout manager
            layoutManager = viewManager

            // specify an viewAdapter (see also next example)
            adapter = viewAdapter

        }

        binding.addButton.setOnClickListener{
            viewModel.addNewTask(binding.newTaskEditText.text.toString())
        }


        return binding.root
    }

}