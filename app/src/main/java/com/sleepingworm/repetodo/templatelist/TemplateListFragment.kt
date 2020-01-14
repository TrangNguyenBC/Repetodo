package com.sleepingworm.repetodo.templatelist

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sleepingworm.repetodo.R
import com.sleepingworm.repetodo.Utils.ItemActionListener
import com.sleepingworm.repetodo.Utils.SwipeToDeleteCallback
import com.sleepingworm.repetodo.database.TaskDatabase
import com.sleepingworm.repetodo.databinding.FragmentTemplateListBinding

class TemplateListFragment : Fragment(), ItemActionListener {
    private lateinit var binding: FragmentTemplateListBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var viewModel: TemplateListViewModel

    override var updatingPosition = -1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_template_list, container, false)
        setHasOptionsMenu(true)
        Log.i("TemplateListFragment", "Open template fragment")

        // Database & view model
        val application = requireNotNull(this.activity).application
        val dataSource = TaskDatabase.getInstance(application).templateListDao
        val viewModelFactory =
            TemplateListViewModelFactory(
                dataSource,
                application
            )
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(TemplateListViewModel::class.java)

        binding.viewModel = viewModel
        binding.setLifecycleOwner(this)



        viewManager = LinearLayoutManager(this.context)
        var viewAdapter = TemplateListRecyclerAdapter(this)
        binding.templateListRecyclerView.adapter = viewAdapter

        // update data set inside the adapter
        viewModel.templateList.observe(viewLifecycleOwner, Observer {
            it?.let {
                viewAdapter.myDataset = it
            }
        })

        recyclerView = binding.templateListRecyclerView.apply {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            setHasFixedSize(true)

            // use a linear layout manager
            layoutManager = viewManager

            // specify an viewAdapter (see also next example)
            adapter = viewAdapter

        }

        // swipe to delete task
        val swipeHandler = object : SwipeToDeleteCallback(context!!) {
            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int {
                if (viewHolder.adapterPosition == updatingPosition) return 0
                else return super.getMovementFlags(recyclerView, viewHolder)
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = recyclerView.adapter as TemplateListRecyclerAdapter
                adapter.removeItem(viewHolder.adapterPosition)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(recyclerView)

        // add a new task
        binding.addFloatButtonTplList.setOnClickListener{
            viewModel.addNewTemplate("")
        }

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.template_list_overflow_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(item,
            view!!.findNavController())
                || super.onOptionsItemSelected(item)
    }

    override fun onItemDelete(id: Long) {
        viewModel.deleteTemplate(id)
    }

    override fun onItemUpdate(id: Long, title: String) {
        viewModel.updateTask(id, title)
        hideSoftKeyboard(activity!!, view!!)
    }
}