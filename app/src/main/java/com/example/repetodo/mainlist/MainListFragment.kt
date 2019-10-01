package com.example.repetodo.mainlist

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.repetodo.R
import com.example.repetodo.database.TaskDatabase
import com.example.repetodo.databinding.FragmentMainListBinding

class MainListFragment : Fragment() {
    private lateinit var binding: FragmentMainListBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val viewModel = MainListViewModel()
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main_list, container, false)
        viewManager = LinearLayoutManager(this.context)
        viewAdapter = MainTaskRecyclerAdapter(viewModel.taskList.value!!)


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
            recyclerView.adapter!!.notifyDataSetChanged()
            Log.i("MainListFragment", binding.newTaskEditText.text.toString())
            Log.i("MainListFragment", viewModel.taskList.value!!.joinToString())
        }


        return binding.root
    }

}