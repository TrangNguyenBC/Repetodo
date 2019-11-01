package com.example.repetodo.mainlist

import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.repetodo.R
import com.example.repetodo.database.TaskDatabase
import com.example.repetodo.databinding.FragmentMainListBinding
import android.view.inputmethod.InputMethodManager
import android.content.Context;
import android.view.*
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI

class MainListFragment : Fragment(), ItemActionListener {
    private lateinit var binding: FragmentMainListBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var viewModel: MainListViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main_list, container, false)
        setHasOptionsMenu(true)
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



        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(p0: RecyclerView, p1: RecyclerView.ViewHolder, p2: RecyclerView.ViewHolder) : Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, position: Int) {
                viewAdapter.removeItem(viewHolder.adapterPosition)
            }
        }

        var itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)

        binding.addButton.setOnClickListener{
            viewModel.addNewTask("")
        }

        binding.hideButton.setOnClickListener{
            viewModel.changeHideSetting()
        }

        viewModel.hideCompletedTasks.observe(viewLifecycleOwner, Observer {value ->
            if (value)
                binding.hideButton.text = "Show completed task"
            else
                binding.hideButton.text = "Hide completed task"
        })

        binding.insertButton.setOnClickListener {
            viewModel.insertTemplate()
        }

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.overflow_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        Log.i("MainListFragment", "Navigate to template list")
        return NavigationUI.onNavDestinationSelected(item!!,
            view!!.findNavController())
                || super.onOptionsItemSelected(item)
    }

    override fun onItemDelete(id: Long) {
        viewModel.deleteTask(id)
    }

    override fun onItemUpdate(id: Long, title: String) {
        viewModel.updateTask(id, title)
        hideSoftKeyboard()
    }

    override fun onItemCheckUpdate(id: Long, checked: Boolean) {
        viewModel.updateTaskStatus(id, checked)
    }

    override fun hideSoftKeyboard() {
        val imm = activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view!!.getWindowToken(), 0)
    }
}

interface ItemActionListener {
    fun onItemDelete(id: Long)
    fun onItemUpdate(id: Long, title: String)
    fun onItemCheckUpdate(id: Long, checked: Boolean)
    fun hideSoftKeyboard()
}