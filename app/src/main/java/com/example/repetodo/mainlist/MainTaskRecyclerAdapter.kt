package com.example.repetodo.mainlist

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.repetodo.R
import kotlinx.android.synthetic.main.fragment_task_item.view.*

class MainTaskRecyclerAdapter (private val myDataset: MutableList<String>) :
    RecyclerView.Adapter<MainTaskRecyclerAdapter.MyViewHolder>() {

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.
    // Each data item is just a string in this case that is shown in a TextView.
    class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view)


    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): MainTaskRecyclerAdapter.MyViewHolder {
        Log.i("MainTaskRecyclerAdapter", "onCreateViewHolder is called")
        // create a new view
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_task_item, parent, false) as View
        // set the view's size, margins, paddings and layout parameters

        return MyViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        Log.i("MainTaskRecyclerAdapter", "bind view holder is working with position $position")
        holder.view.taskTitle.text = myDataset[position]
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = myDataset.size
}
