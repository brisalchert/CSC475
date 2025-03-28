package com.example.steamtracker.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.steamtracker.room.dao.AppDetailsDao
import com.example.steamtracker.room.dao.CollectionsDao
import com.example.steamtracker.room.dao.NewsAppsDao
import com.example.steamtracker.room.dao.SpyDao
import com.example.steamtracker.room.dao.SteamworksDao
import com.example.steamtracker.room.dao.StoreDao
import com.example.steamtracker.room.entities.AppDetailsEntity
import com.example.steamtracker.room.entities.AppInfoEntity
import com.example.steamtracker.room.entities.AppNewsEntity
import com.example.steamtracker.room.entities.AppNewsRequestEntity
import com.example.steamtracker.room.entities.CollectionAppEntity
import com.example.steamtracker.room.entities.CollectionEntity
import com.example.steamtracker.room.entities.FeaturedCategoryEntity
import com.example.steamtracker.room.entities.NewsAppEntity
import com.example.steamtracker.room.entities.NewsItemEntity
import com.example.steamtracker.room.entities.SpotlightItemEntity
import com.example.steamtracker.room.entities.SteamSpyAppEntity
import com.example.steamtracker.room.entities.TagEntity

@Database(
    entities = [
        FeaturedCategoryEntity::class,
        AppInfoEntity::class,
        SpotlightItemEntity::class,
        SteamSpyAppEntity::class,
        TagEntity::class,
        AppNewsRequestEntity::class,
        AppNewsEntity::class,
        NewsItemEntity::class,
        NewsAppEntity::class,
        AppDetailsEntity::class,
        CollectionEntity::class,
        CollectionAppEntity::class
    ],
    version = 2,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun storeDao(): StoreDao
    abstract fun salesDao(): SpyDao
    abstract fun steamworksDao(): SteamworksDao
    abstract fun newsAppsDao(): NewsAppsDao
    abstract fun appDetailsDao(): AppDetailsDao
    abstract fun collectionsDao(): CollectionsDao

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
