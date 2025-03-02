package com.example.todolist

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.color.MaterialColors

class TodoAdapter (
    private val mainActivity: MainActivity
): ListAdapter<ListItem, TodoAdapter.ListItemHolder>(TodoDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.listitem, parent, false)

        return ListItemHolder(itemView)
    }

    override fun onBindViewHolder(holder: ListItemHolder, position: Int) {
        val todo = getItem(position)

        // Set new holder values
        holder.title.text = todo.title
        holder.description.text = todo.description
        if (todo.important) {
            // Update important colors
            val backgroundColor = MaterialColors.getColor(mainActivity, com.google.android.material.R.attr.colorTertiaryContainer, Color.WHITE)
            val textColor = MaterialColors.getColor(mainActivity, com.google.android.material.R.attr.colorOnTertiaryContainer, Color.BLACK)
            holder.itemView.setBackgroundColor(backgroundColor)
            holder.title.setTextColor(textColor)
            holder.description.setTextColor(textColor)
        } else {
            // Use regular colors
            val backgroundColor = MaterialColors.getColor(mainActivity, com.google.android.material.R.attr.colorSurface, Color.WHITE)
            val textColor = MaterialColors.getColor(mainActivity, com.google.android.material.R.attr.colorOnSurface, Color.BLACK)
            holder.itemView.setBackgroundColor(backgroundColor)
            holder.title.setTextColor(textColor)
            holder.description.setTextColor(textColor)
        }

        // Reset completion change listener for reuse
        holder.completedBox.setOnCheckedChangeListener(null)

        holder.completedBox.isChecked = todo.completed

        // Set completion change listener
        holder.completedBox.setOnCheckedChangeListener { _, isChecked ->
            todo.completed = isChecked
            mainActivity.updateCompletion(todo)
        }

        // Set deletion change listener
        holder.deleteButton.setOnClickListener {
            showDeleteDialog(mainActivity) {
                // Remove completion status for recycled views
                holder.completedBox.isChecked = false

                mainActivity.deleteListItem(todo)
            }
        }
    }

    /**
     * Creates a dialog window to confirm that the user wants to delete a list item
     */
    private fun showDeleteDialog(context: Context, onConfirm: () -> Unit) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Delete To-Do List Item")
        builder.setMessage("Are you sure you want to delete this item?")

        builder.setPositiveButton("Delete") { dialog, _ ->
            // Call deletion function and indicate success to user
            onConfirm()
            Toast.makeText(context, "List Item deleted.", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }

        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }

        builder.create().show()
    }

    inner class ListItemHolder(view: View): RecyclerView.ViewHolder(view) {
        internal var title: TextView = view.findViewById(R.id.textViewTitle)
        internal var description: TextView = view.findViewById(R.id.textViewDescription)
        internal var completedBox: CheckBox = view.findViewById(R.id.checkBoxCompleted)
        internal var deleteButton: ImageButton = view.findViewById(R.id.deleteButton)
    }
}