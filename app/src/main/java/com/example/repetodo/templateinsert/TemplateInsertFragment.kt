package com.example.repetodo.templateinsert

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.repetodo.R
import com.example.repetodo.Utils.ItemActionListener
import com.example.repetodo.database.TaskDatabase
import com.example.repetodo.databinding.FragmentTemplateListInsertBinding

class TemplateInsertFragment : Fragment(), ItemActionListener {

    private lateinit var binding: FragmentTemplateListInsertBinding

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var viewModel: TemplateInsertViewModel

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

    override fun onItemDelete(id: Long) {
        // do nothing
    }

    override fun onItemUpdate(id: Long, title: String) {
        // do nothing
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
        viewModel.insertFromTemplate(id)
        Navigation.createNavigateOnClickListener(R.id.action_templateInsertFragment_to_mainListFragment)
    }
}