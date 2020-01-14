package com.sleepingworm.repetodo.templatelist

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sleepingworm.repetodo.R
import android.view.KeyEvent
import androidx.navigation.findNavController
import com.sleepingworm.repetodo.Utils.ItemActionListener
import com.sleepingworm.repetodo.database.Template
import kotlinx.android.synthetic.main.fragment_template_item.view.*
import kotlinx.android.synthetic.main.fragment_template_list.view.*
import kotlinx.android.synthetic.main.fragment_template_list_item.view.*

class TemplateListRecyclerAdapter(private var itemActionListener: ItemActionListener):
    RecyclerView.Adapter<TemplateListRecyclerAdapter.MyViewHolder>() {
    var myDataset = listOf<Template>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.
    class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view)


    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): MyViewHolder {
        // create a new view
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_template_list_item, parent, false) as View
        // set the view's size, margins, paddings and layout parameters

        return MyViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var id = myDataset[position].templateId
        var title = myDataset[position].templateTitle
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        holder.view.detailedInfoButton.setOnClickListener {
            it.findNavController().navigate(TemplateListFragmentDirections.actionTemplateListFragmentToTemplateFragment(id, title))
        }

        holder.view.templateTitle.apply {
            // show the task title
            setText(title)
            // focus cursor to the end of task title
            setSelection(title.length)
        }

        holder.view.templateTitle.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                var newTitle = holder.view.templateTitle.text.toString()
                itemActionListener.onItemUpdate(id, newTitle)
                itemActionListener.updatingPosition = -1
            } else {
                itemActionListener.updatingPosition = position
            }
        }

        holder.view.templateTitle.setOnKeyListener(View.OnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER || event.action == KeyEvent.ACTION_UP) {
                Log.i("TemplateRecyclerAdapter", "Heard an Enter Key")
                Log.i("TemplateRecyclerAdapter", "ID is $id")
                var newTitle = holder.view.templateTitle.text.toString()
                itemActionListener.onItemUpdate(id, newTitle)
            }
            false
        })
    }

    fun getIdFromPosition(position: Int): Long {
        return myDataset[position].templateId
    }
    fun removeItem(position: Int) {
        itemActionListener.onItemDelete(myDataset[position].templateId)
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = myDataset.size
}
