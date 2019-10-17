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
import com.example.repetodo.database.TaskDatabase
import com.example.repetodo.databinding.FragmentMainListBinding

interface ItemDeleteListener {
    fun onItemDelete(position: Int)
}

class MainListFragment : Fragment(), ItemDeleteListener {
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
        // viewModel = ViewModelProviders.of(this).get(MainListViewModel::class.java)

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main_list, container, false)
        // binding.viewModel = viewModel

        // Database & view model
        val application = requireNotNull(this.activity).application
        val dataSource = TaskDatabase.getInstance(application).taskDatabaseDao
        val viewModelFactory = MainListViewModelFactory(dataSource, application)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MainListViewModel::class.java)

        binding.viewModel = viewModel
        binding.setLifecycleOwner(this)



        viewManager = LinearLayoutManager(this.context)
        var viewAdapter = MainTaskRecyclerAdapter(this)
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

    override fun onItemDelete(position: Int) {
        viewModel.deleteTask(position)
    }

}