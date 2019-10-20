package com.example.repetodo.mainlist

import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.repetodo.R
import com.example.repetodo.database.TaskInformation
import kotlinx.android.synthetic.main.fragment_task_item.view.*
import java.lang.NullPointerException

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
        Log.i("MainTaskRecyclerAdapter", "onCreateViewHolder is called")
        // create a new view
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_task_item, parent, false) as View
        // set the view's size, margins, paddings and layout parameters

        return MyViewHolder(view)
    }

    fun changeBtnStatus(holder: MyViewHolder, status: Int) {
        holder.view.deleteBtn.visibility = status
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var id = myDataset[position].taskId
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        Log.i("MainTaskRecyclerAdapter", "bind view holder is working with position $position")

        holder.view.taskTitle.apply {
            // show the task title
            setText(myDataset[position].taskTitle)
            // focus cursor to the end of task title
            setSelection(myDataset[position].taskTitle.toString().length)
        }

        // set the status
        holder.view.checkBox.isChecked = (myDataset[position].taskStatus == 1)
        holder.view.deleteBtn.visibility = View.INVISIBLE

        holder.view.checkBox.setOnClickListener {
            Log.i("MainTaskRecyclerAdapter", "Check box $position is clicked")
            if (holder.view.checkBox.isChecked) changeBtnStatus(holder, View.VISIBLE)
            else changeBtnStatus(holder, View.INVISIBLE)
        }
        holder.view.deleteBtn.setOnClickListener {
            Log.i("MainTaskRecyclerAdapter", "Item $position should be deleted")

            itemActionListener.onItemDelete(id)
        }

        holder.view.taskTitle.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                var newTitle = holder.view.taskTitle.text.toString()
                Log.i("MainTaskRecyclerAdapter", "Item $id should be updated to $newTitle")
                itemActionListener.onItemUpdate(id, newTitle)
            }
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = myDataset.size
}
