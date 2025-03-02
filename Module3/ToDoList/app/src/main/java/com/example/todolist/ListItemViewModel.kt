package com.example.todolist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ListItemViewModel(application: Application) : AndroidViewModel(application) {
    private val listItemDao = AppDatabase.getInstance(application).listItemDao()
    val allItems: LiveData<List<ListItemEntity>> = listItemDao.getAllItems()

    // Keep track of the number of completed items
    private val _numCompleted = MutableLiveData<Int>()
    val numCompleted: LiveData<Int> get() = _numCompleted

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

        if (item.completed) {
            _numCompleted.value = _numCompleted.value?.minus(1)
        }
    }

    fun update(item: ListItem) {
        viewModelScope.launch(Dispatchers.IO) {
            listItemDao.update(ListItemEntity.fromListItem(item))
        }

        // Update numCompleted
        if (item.completed) {
            _numCompleted.value = _numCompleted.value?.plus(1)
        } else {
            _numCompleted.value = _numCompleted.value?.minus(1)
        }
    }

    fun initNumCompleted() {
        val itemList = allItems.value?.map { it.toListItem() }
        var num = 0

        if (itemList != null) {
            for (item in itemList) {
                if (item.completed) num++
            }
        }

        _numCompleted.value = num
    }
}
