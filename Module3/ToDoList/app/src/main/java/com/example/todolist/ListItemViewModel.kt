package com.example.todolist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ListItemViewModel(application: Application) : AndroidViewModel(application) {
    private val listItemDao = AppDatabase.getInstance(application).listItemDao()
    val allItems: LiveData<List<ListItemEntity>> = listItemDao.getAllItems()

    fun insert(item: ListItem, callback: (Int) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val newId = listItemDao.insert(ListItemEntity.fromListItem(item)).toInt()
            callback(newId) // Provide new ID value to MainActivity
        }
    }

    fun delete(item: ListItem) {
        viewModelScope.launch(Dispatchers.IO) {
            listItemDao.delete(ListItemEntity.fromListItem(item))
        }
    }

    fun update(item: ListItem) {
        viewModelScope.launch(Dispatchers.IO) {
            listItemDao.update(ListItemEntity.fromListItem(item))
        }
    }
}
