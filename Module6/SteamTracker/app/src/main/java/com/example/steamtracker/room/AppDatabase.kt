package com.example.steamtracker.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.steamtracker.room.dao.FeaturedCategoriesDao
import com.example.steamtracker.room.entities.AppInfoEntity
import com.example.steamtracker.room.entities.FeaturedCategoryEntity
import com.example.steamtracker.room.entities.SpotlightItemEntity

@Database(
    entities = [
        FeaturedCategoryEntity::class,
        AppInfoEntity::class,
        SpotlightItemEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase: RoomDatabase() {
    abstract fun FeaturedCategoriesDao(): FeaturedCategoriesDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
