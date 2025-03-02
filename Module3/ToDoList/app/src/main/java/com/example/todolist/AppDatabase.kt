package com.example.todolist

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ListItemEntity::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun listItemDao(): ListItemDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            // Get database or create a new one if necessary
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "todolist_database"
                ).fallbackToDestructiveMigration()
                    .build().also { INSTANCE = it }
            }
        }
    }
}