package com.example.todolist

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ListItemDao {
    @Insert
    suspend fun insert(listItem: ListItemEntity): Long // Return item ID

    @Update
    suspend fun update(listItem: ListItemEntity)

    @Delete
    suspend fun delete(listItem: ListItemEntity)

    @Query("SELECT * FROM list_items")
    fun getAllItems(): LiveData<List<ListItemEntity>>
}