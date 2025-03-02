package com.example.todolist

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.core.view.WindowInsetsControllerCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.databinding.ActivityMainBinding
import java.util.Collections

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: ListItemViewModel by viewModels()

    private var numCompleted = 0 // Used to track position of last uncompleted item
    private var recyclerView: RecyclerView? = null
    private var adapter: TodoAdapter? = null

    @SuppressLint("NotifyDataSetChanged")
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

        // Set up RecyclerView
        recyclerView = findViewById<View>(R.id.recyclerView) as RecyclerView
        adapter = TodoAdapter(this)

        recyclerView!!.layoutManager = LinearLayoutManager(applicationContext)
        recyclerView!!.itemAnimator = DefaultItemAnimator()

        // Divider for list items
        recyclerView!!.addItemDecoration(
            DividerItemDecoration(this, LinearLayoutManager.VERTICAL))

        recyclerView!!.adapter = adapter

        // Observe LiveData from ViewModel
        viewModel.allItems.observe(this) { items ->
            val sortedItems = items.map { it.toListItem() }.sortedBy { it.completed }
            adapter!!.submitList(sortedItems)
        }
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
        val index = if (adapter!!.currentList.isNotEmpty()) {
            adapter!!.currentList.size - numCompleted
        } else {
            0
        }

        // Update database and todoList
        viewModel.insert(item) { newId ->
            runOnUiThread {
                item.id = newId // Use database's auto-generated ID value
                val updatedList = ArrayList(adapter!!.currentList)
                updatedList.add(index, item)
                adapter!!.submitList(updatedList)
            }
        }
    }

    fun deleteListItem(item: ListItem) {
        // Update database
        viewModel.delete(item)

        runOnUiThread {
            val updatedList = ArrayList(adapter!!.currentList)
            updatedList.remove(item)
            adapter!!.submitList(updatedList)
        }
    }

    fun updateCompletion(item: ListItem) {
        // Update item completion in database
        viewModel.update(item)

        runOnUiThread {
            val updatedList = ArrayList(adapter!!.currentList)
            val newIndex: Int

            updatedList.remove(item)

            if (item.completed) {
                numCompleted++
                newIndex = updatedList.size
            } else {
                numCompleted--
                newIndex = 0
            }

            updatedList.add(newIndex, item)
            adapter!!.submitList(updatedList)
        }
    }
}