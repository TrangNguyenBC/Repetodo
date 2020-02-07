package com.sleepingworm.repetodo.mainlist

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sleepingworm.repetodo.R
import com.sleepingworm.repetodo.database.TaskInformation
import kotlinx.android.synthetic.main.fragment_task_item.view.*
import android.view.KeyEvent
import com.sleepingworm.repetodo.Utils.ItemActionListener

class MainTaskRecyclerAdapter(private var itemActionListener: ItemActionListener):
    RecyclerView.Adapter<MainTaskRecyclerAdapter.MyViewHolder>() {
    var myDataset = listOf<TaskInformation>()
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
        //Log.i("MainTaskRecyclerAdapter", "onCreateViewHolder is called")
        // create a new view
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_task_item, parent, false) as View
        // set the view's size, margins, paddings and layout parameters

        return MyViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var id = myDataset[position].taskId
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        //Log.i("MainTaskRecyclerAdapter", "bind view holder is working with position $position")

        holder.view.taskTitle.apply {
            // show the task title
            setText(myDataset[position].taskTitle)
            // focus cursor to the end of task title
            setSelection(myDataset[position].taskTitle.toString().length)
        }

        // set the status
        holder.view.checkBox.isChecked = (myDataset[position].taskStatus == 1)

        holder.view.checkBox.setOnClickListener {
            itemActionListener.onItemCheckUpdate(id, holder.view.checkBox.isChecked)
            //Log.i("MainTaskRecyclerAdapter", "Check box $position is clicked")
        }

        holder.view.taskTitle.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                var newTitle = holder.view.taskTitle.text.toString()
                Log.i("MainTaskRecyclerAdapter", "Item $id should be updated to $newTitle")
                itemActionListener.onItemUpdate(id, newTitle)
                itemActionListener.updatingPosition = -1
            } else {
                itemActionListener.updatingPosition = position
                itemActionListener.changeAddButtonVisibility(true)
            }

        }

        holder.view.taskTitle.setOnKeyListener(View.OnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                var newTitle = holder.view.taskTitle.text.toString()
                //Log.i("MainTaskRecyclerAdapter", "Item $id should be updated to $newTitle")
                itemActionListener.onItemUpdate(id, newTitle)
            }
            false
        })
    }

    fun removeItem(position: Int) {
        itemActionListener.onItemDelete(myDataset[position].taskId)
        Log.i("MainTaskRecyclerAdapter", "Receive the request to delete $position")
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = myDataset.size
}
