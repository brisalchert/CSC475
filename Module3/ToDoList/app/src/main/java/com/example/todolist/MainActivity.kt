package com.example.todolist

import android.os.Bundle
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
    private var recyclerView: RecyclerView? = null
    private var adapter: TodoAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(findViewById(R.id.toolbar))

        // Ensure the status bar icons have proper contrast
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = false

        binding.fab.setOnClickListener { view ->
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
        todoList.add(item)
        adapter!!.notifyItemInserted(todoList.indexOf(item))
    }
}