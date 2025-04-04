package com.example.steamtracker.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import com.example.steamtracker.TestDatabase
import com.example.steamtracker.fake.FakeCollectionApp
import com.example.steamtracker.room.dao.CollectionsDao
import com.example.steamtracker.room.entities.CollectionAppEntity
import com.example.steamtracker.room.entities.CollectionEntity
import com.example.steamtracker.room.relations.CollectionWithApps
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.equalTo
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.IOException

class CollectionsDaoTest {
    private lateinit var collectionsDao: CollectionsDao
    private lateinit var db: TestDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context,
            TestDatabase::class.java
        ).build()
        collectionsDao = db.collectionsDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun writeCollectionAndReadInList() =
        runTest {
            val collectionEntity = CollectionEntity(FakeCollectionApp.response.collectionName)
            val collectionAppEntity = CollectionAppEntity(
                collectionName = FakeCollectionApp.response.collectionName,
                appid = FakeCollectionApp.response.appId,
                index = FakeCollectionApp.response.index
            )
            val collectionWithApps = CollectionWithApps(
                collection = collectionEntity,
                collectionAppsDetails = listOf(collectionAppEntity)
            )

            collectionsDao.insertCollection(collectionEntity)
            collectionsDao.insertCollectionApp(collectionAppEntity)

            var allCollections = collectionsDao.getAllCollections().first()

            // Test collection fetched
            assertThat(allCollections[0], equalTo(collectionWithApps))

            collectionsDao.removeCollection(FakeCollectionApp.response.collectionName)

            allCollections = collectionsDao.getAllCollections().first()

            // Test collection removed
            assertThat(allCollections, equalTo(emptyList()))
        }

    @Test
    @Throws(Exception::class)
    fun writeCollectionAndReadByName() =
        runTest {
            val collectionEntity = CollectionEntity(FakeCollectionApp.response.collectionName)
            val collectionAppEntity = CollectionAppEntity(
                collectionName = FakeCollectionApp.response.collectionName,
                appid = FakeCollectionApp.response.appId,
                index = FakeCollectionApp.response.index
            )
            val collectionWithApps = CollectionWithApps(
                collection = collectionEntity,
                collectionAppsDetails = listOf(collectionAppEntity)
            )

            collectionsDao.insertCollection(collectionEntity)
            collectionsDao.insertCollectionApp(collectionAppEntity)

            var collection = collectionsDao.getCollectionByName(FakeCollectionApp.response.collectionName)

            // Test collection fetched
            assertThat(collection, equalTo(collectionWithApps))

            collectionsDao.removeCollection(FakeCollectionApp.response.collectionName)

            collection = collectionsDao.getCollectionByName(FakeCollectionApp.response.collectionName)

            // Test collection removed
            assertThat(collection, equalTo(null))
        }
}
