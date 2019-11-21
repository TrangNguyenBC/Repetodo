package com.example.repetodo.templatelist

import android.content.Context
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
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.repetodo.R
import com.example.repetodo.Utils.ItemActionListener
import com.example.repetodo.database.TaskDatabase
import com.example.repetodo.databinding.FragmentTemplateListBinding

class TemplateListFragment : Fragment(), ItemActionListener {
    private lateinit var binding: FragmentTemplateListBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var viewModel: TemplateListViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_template_list, container, false)
        Log.i("TemplateListFragment", "Open template fragment")

//        binding.addTemplateTaskButton.setOnClickListener{
//            viewModel.addNewTask("")
//        }

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

        // swipe left to edit the template items
        val itemTouchHelperCallbackForDetailed = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(p0: RecyclerView, p1: RecyclerView.ViewHolder, p2: RecyclerView.ViewHolder) : Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, position: Int) {
                Log.i("TemplateListFragment", "navigate to template fragment")
                var templateId = viewAdapter.getIdFromPosition(viewHolder.adapterPosition)
                view!!.findNavController().navigate(TemplateListFragmentDirections.actionTemplateListFragmentToTemplateFragment(templateId))


                //viewAdapter.navigateToTemplateFragment(viewHolder.adapterPosition)
            }
        }

        var itemTouchHelperForDetailed = ItemTouchHelper(itemTouchHelperCallbackForDetailed)
        itemTouchHelperForDetailed.attachToRecyclerView(recyclerView)

        // add a new task
        binding.addFloatButtonTplList.setOnClickListener{
            viewModel.addNewTemplate("")
        }

        return binding.root
    }

    override fun onItemDelete(id: Long) {
        viewModel.deleteTemplate(id)
    }

    override fun onItemUpdate(id: Long, title: String) {
        viewModel.updateTask(id, title)
        hideSoftKeyboard()
    }

    override fun onItemCheckUpdate(id: Long, checked: Boolean) {
        // do nothing
    }

    override fun hideSoftKeyboard() {
        val imm = activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view!!.getWindowToken(), 0)
    }

    override fun onInsertTemplate(id: Long) {
        //do nothing
    }
}