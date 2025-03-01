package com.example.todolist

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.core.view.WindowInsetsControllerCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val todoList = ArrayList<ListItem>()
    private var numCompleted = 0 // Used to track position of last uncompleted item
    private var recyclerView: RecyclerView? = null
    private var adapter: TodoAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(findViewById(R.id.toolbar))

        // Ensure the status bar icons have proper contrast
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = false

        binding.fab.setOnClickListener {
            val dialog = DialogNewListItem()
            dialog.show(supportFragmentManager, "")
        }

        recyclerView = findViewById<View>(R.id.recyclerView) as RecyclerView
        adapter = TodoAdapter(this, todoList)
        val layoutManager = LinearLayoutManager(applicationContext)

        recyclerView!!.layoutManager = layoutManager
        recyclerView!!.itemAnimator = DefaultItemAnimator()

        // Divider for list items
        recyclerView!!.addItemDecoration(
            DividerItemDecoration(this, LinearLayoutManager.VERTICAL))

        recyclerView!!.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun createNewListItem(item: ListItem) {
        // Add item before completed items
        val index = todoList.size - numCompleted
        todoList.add(index, item)
        adapter!!.notifyItemInserted(index)
    }

    fun deleteListItem(item: ListItem) {
        val index = todoList.indexOf(item)
        if (index != -1) {
            todoList.remove(item)

            // Prevent concurrent RecyclerView updates with list modification
            Handler(Looper.getMainLooper()).post {
                adapter!!.notifyItemRemoved(index)
            }
        }
    }

    fun updateCompletion(item: ListItem) {
        val oldIndex = todoList.indexOf(item)
        val newIndex: Int
        if (oldIndex == -1) return // Prevent crash when item not found

        if (!item.completed) {
            // Move item to the top of the list
            newIndex = 0
            numCompleted--
        } else {
            // Move item to the bottom of the list
            newIndex = todoList.size - 1
            numCompleted++
        }

        if (oldIndex != newIndex) {
            val removedItem = todoList.removeAt(oldIndex)

            todoList.add(newIndex, removedItem)

            // Prevent concurrent RecyclerView updates with list modification
            Handler(Looper.getMainLooper()).post {
                adapter!!.notifyItemMoved(oldIndex, newIndex)
                adapter!!.notifyItemChanged(newIndex)
            }
        }
    }
}