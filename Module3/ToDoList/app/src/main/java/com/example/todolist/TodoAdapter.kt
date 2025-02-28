package com.example.todolist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TodoAdapter (
    private val mainActivity: MainActivity,
    private val todoList: List<ListItem>
): RecyclerView.Adapter<TodoAdapter.ListItemHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.listitem, parent, false)

        return ListItemHolder(itemView)
    }

    override fun onBindViewHolder(holder: ListItemHolder, position: Int) {
        val todo = todoList[position]
        holder.title.text = todo.title

        holder.description.text = todo.description

        // Check if the list item is marked as important
        if (todo.important) {
            holder.important.text = mainActivity.resources.getString(R.string.important_text)
        }
    }

    override fun getItemCount(): Int {
        if (todoList != null) {
            return todoList.size
        }

        return -1
    }

    inner class ListItemHolder(view: View): RecyclerView.ViewHolder(view) {
        internal var title = view.findViewById<View>(R.id.textViewTitle) as TextView
        internal var description = view.findViewById<View>(R.id.textViewDescription) as TextView
        internal var important = view.findViewById<View>(R.id.textViewImportant) as TextView

        init {
            view.isClickable = true
        }
    }
}