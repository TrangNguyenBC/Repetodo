package com.example.repetodo.template

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.repetodo.R
import com.example.repetodo.Utils.ItemActionListener
import com.example.repetodo.database.TaskDatabase
import com.example.repetodo.databinding.FragmentTemplateBinding
import com.example.repetodo.mainlist.MainListViewModelFactory
import com.google.android.material.snackbar.Snackbar

class TemplateFragment : Fragment(), ItemActionListener {
    private lateinit var binding: FragmentTemplateBinding

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var viewModel: TemplateViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_template, container, false)

        var args = TemplateFragmentArgs.fromBundle(arguments!!)
        Log.i("TemplateFragment", "xin chao ${args.fragmentId}")

        // Database & view model
        val application = requireNotNull(this.activity).application
        val dataSource = TaskDatabase.getInstance(application).templateDao
        val viewModelFactory =
            TemplateViewModelFactory(
                dataSource,
                application,
                args.fragmentId
            )
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(TemplateViewModel::class.java)

        binding.viewModel = viewModel
        binding.setLifecycleOwner(this)



        viewManager = LinearLayoutManager(this.context)
        var viewAdapter = TemplateRecyclerAdapter(this)
        binding.templateItemRecyclerView.adapter = viewAdapter

        // update data set inside the adapter
        viewModel.templateTaskList.observe(viewLifecycleOwner, Observer {
            it?.let {
                viewAdapter.myDataset = it
            }
        })

        recyclerView = binding.templateItemRecyclerView.apply {
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

//            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, position: Int) {
//                viewAdapter.removeItem(viewHolder.adapterPosition)
//
//            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                Log.i("TemplateFragment", "Direction is $direction")
                viewAdapter.removeItem(viewHolder.adapterPosition)
                if (direction == ItemTouchHelper.RIGHT) {
                    Log.i("TemplateFragment", "Show taskbar")
                    // showing snack bar with Undo option
                    val snackbar = Snackbar.make(view!!, "The item was deleted",Snackbar.LENGTH_SHORT)
//                    snackbar.setAction("UNDO") {
//                        // undo is selected, restore the deleted item
//                        adapter!!.restoreItem(deletedModel, position)
//                    }
//                    snackbar.setActionTextColor(Color.YELLOW)
                    snackbar.show()
                }
//                } else {
//                    val deletedModel = imageModelArrayList!![position]
//                    adapter!!.removeItem(position)
//                    // showing snack bar with Undo option
//                    val snackbar = Snackbar.make(
//                        window.decorView.rootView,
//                        " removed from Recyclerview!",
//                        Snackbar.LENGTH_LONG
//                    )
//                    snackbar.setAction("UNDO") {
//                        // undo is selected, restore the deleted item
//                        adapter!!.restoreItem(deletedModel, position)
//                    }
//                    snackbar.setActionTextColor(Color.YELLOW)
//                    snackbar.show()
//                }
            }
        }

        var itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)

        // add a new task
        binding.addFloatButtonTpl.setOnClickListener{
            viewModel.addNewTask("")
        }

        return binding.root
    }

    override fun onItemDelete(id: Long) {
        viewModel.deleteItem(id)
    }

    override fun onItemUpdate(id: Long, title: String) {
        viewModel.updateItem(id, title)
        hideSoftKeyboard(activity!!, view!!)
    }
}