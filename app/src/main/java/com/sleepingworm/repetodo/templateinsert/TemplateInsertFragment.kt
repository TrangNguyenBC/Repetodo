package com.sleepingworm.repetodo.templateinsert

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sleepingworm.repetodo.R
import com.sleepingworm.repetodo.Utils.ItemActionListener
import com.sleepingworm.repetodo.database.TaskDatabase
import com.sleepingworm.repetodo.databinding.FragmentTemplateListInsertBinding

class TemplateInsertFragment : Fragment(), ItemActionListener {

    private lateinit var binding: FragmentTemplateListInsertBinding

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var viewModel: TemplateInsertViewModel

    override var updatingPosition = -1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_template_list_insert, container, false)

        val application = requireNotNull(this.activity).application
        val dataSource = TaskDatabase.getInstance(application).templateListDao
        val templateDao = TaskDatabase.getInstance(application).templateDao
        val taskListDao = TaskDatabase.getInstance(application).taskListDao
        val viewModelFactory =
            TemplateInsertViewModelFactory(
                dataSource,
                templateDao,
                taskListDao,
                application
            )
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(TemplateInsertViewModel::class.java)

        binding.viewModel = viewModel
        binding.setLifecycleOwner(this)


        viewManager = LinearLayoutManager(this.context)
        var viewAdapter = TplInsertRecAdapter(this)
        binding.templateListInsertRecyclerView.adapter = viewAdapter

        // update data set inside the adapter
        viewModel.templateInsertList.observe(viewLifecycleOwner, Observer {
            it?.let {
                viewAdapter.myDataset = it
            }
        })

        recyclerView = binding.templateListInsertRecyclerView.apply {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            setHasFixedSize(true)

            // use a linear layout manager
            layoutManager = viewManager

            // specify an viewAdapter (see also next example)
            adapter = viewAdapter

        }

        return binding.root
    }

    override fun onInsertTemplate(id: Long) {
        binding.templateListInsertRecyclerView.visibility = View.INVISIBLE
        binding.progressBar.visibility = View.VISIBLE
        viewModel.insertFromTemplate(id)
        viewModel.finishInsertion.observe(viewLifecycleOwner, Observer {
            if (viewModel.finishInsertion.value!!) {
                Navigation.createNavigateOnClickListener(R.id.action_templateInsertFragment_to_mainListFragment)
                Log.i("TemplateInsertFragment", "Navigate from TemplateInsert to Mainlist")
            }
        })
    }
}