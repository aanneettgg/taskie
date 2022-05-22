package com.example.taskie.ui.main

import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.taskie.R
import com.example.taskie.TaskFragment
import com.example.taskie.database.Task

/**
 * An adapter handling task item.
 */
class TaskAdapter(
    private var tasks: List<Task>,
    private val fragment: TaskFragment,
    private val context: Context) : RecyclerView.Adapter<TaskAdapter.ViewHolder>() {


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val taskName = itemView.findViewById<TextView>(R.id.textViewTaskName)!!
        val checkBox = itemView.findViewById<CheckBox>(R.id.checkBoxTask)!!
        val taskType = itemView.findViewById<ImageView>(R.id.imageViewTaskType)!!
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskAdapter.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val taskView = inflater.inflate(R.layout.item, parent, false)
        return ViewHolder(taskView)
    }

    override fun onBindViewHolder(viewHolder: TaskAdapter.ViewHolder, position: Int) {
        val task: Task = tasks[position]
        val taskName = viewHolder.taskName
        taskName.text = task.taskName
        val taskType = viewHolder.taskType
        if (!task.taskType) {
            taskType.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_briefcase))
        }
        if (task.isCompleted) {
            viewHolder.taskName.paintFlags = viewHolder.taskName.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            viewHolder.checkBox.isChecked = true
        }

        viewHolder.itemView.setOnClickListener {
            viewHolder.checkBox.isChecked = !viewHolder.checkBox.isChecked
            if (viewHolder.checkBox.isChecked) {
                viewHolder.taskName.paintFlags = viewHolder.taskName.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                fragment.setItemState(task)
            } else {
                viewHolder.taskName.paintFlags = viewHolder.taskName.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                fragment.setItemState(task)
            }
        }

        viewHolder.itemView.setOnLongClickListener {
            fragment.editItem(task)
            return@setOnLongClickListener true
        }
    }

    override fun getItemCount(): Int {
        return tasks.size
    }
}