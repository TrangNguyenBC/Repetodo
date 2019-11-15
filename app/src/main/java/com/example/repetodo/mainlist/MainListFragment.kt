package com.example.repetodo.mainlist

import android.os.Bundle
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
import android.util.Log
import android.view.*
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.repetodo.Utils.ItemActionListener
import com.example.repetodo.template.TemplateFragmentArgs
import timber.log.Timber

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
        Log.i("MainListFragment", "MainListOnCreateView is called")

        // Database & view model
        val application = requireNotNull(this.activity).application
        val dataSource = TaskDatabase.getInstance(application).taskListDao
        val viewModelFactory =
            MainListViewModelFactory(
                dataSource,
                application
            )
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

        // swipe to delete task
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            override fun onMove(p0: RecyclerView, p1: RecyclerView.ViewHolder, p2: RecyclerView.ViewHolder) : Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, position: Int) {
                viewAdapter.removeItem(viewHolder.adapterPosition)
            }
        }

        var itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)

        // add a new task
        binding.addButton.setOnClickListener{
            viewModel.addNewTask("")
        }

        // show/hide completed task setting
        binding.hideButton.setOnClickListener{
            viewModel.changeHideSetting()
        }

        viewModel.hideCompletedTasks.observe(viewLifecycleOwner, Observer {value ->
            if (value)
                binding.hideButton.text = "Show completed task"
            else
                binding.hideButton.text = "Hide completed task"
        })

        binding.insertButton.setOnClickListener{
            //Navigation.createNavigateOnClickListener(R.id.action_mainListFragment_to_templateInsertFragment)
            view!!.findNavController().navigate(R.id.action_mainListFragment_to_templateInsertFragment)
            Log.i("MainListFragment", "Navigate from Main List to TemplateInsert")
        }


        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.overflow_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(item,
            view!!.findNavController())
                ||super.onOptionsItemSelected(item)
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

    override fun onInsertTemplate(id: Long) {
        //do nothing
    }
}