package com.example.steamtracker.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import com.example.steamtracker.TestDatabase
import com.example.steamtracker.fake.FakeFeaturedCategoriesRequest
import com.example.steamtracker.room.dao.StoreDao
import com.example.steamtracker.room.relations.FeaturedCategoryWithDetails
import com.example.steamtracker.utils.mapToAppInfoEntities
import com.example.steamtracker.utils.mapToFeaturedCategoryEntities
import com.example.steamtracker.utils.mapToSpotlightItemEntities
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.equalTo
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.IOException

class StoreDaoTest {
    private lateinit var storeDao: StoreDao
    private lateinit var db: TestDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context,
            TestDatabase::class.java
        ).build()
        storeDao = db.storeDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    @Throws(Exception::class)
    fun writeFeaturedCategoriesAndReadInList() =
        runTest {
            val fakeFeaturedCategoryEntities = FakeFeaturedCategoriesRequest.response.mapToFeaturedCategoryEntities()
            val fakeAppInfoEntities = FakeFeaturedCategoriesRequest.response.mapToAppInfoEntities()
            val fakeSpotlightItemEntities = FakeFeaturedCategoriesRequest.response.mapToSpotlightItemEntities()
            val fakeFeaturedCategoriesWithDetails = fakeFeaturedCategoryEntities.map {
                FeaturedCategoryWithDetails(
                    category = it,
                    appItems = fakeAppInfoEntities,
                    spotlightItems = fakeSpotlightItemEntities
                )
            }

            storeDao.insertFeaturedCategoryWithDetails(
                categoryEntities = fakeFeaturedCategoryEntities,
                appInfoEntities = fakeAppInfoEntities,
                spotlightItemEntities = fakeSpotlightItemEntities
            )

            var allFeaturedCategories = storeDao.getAllFeaturedCategories().first()

            // Test FeaturedCategories fetched
            assertThat(allFeaturedCategories, equalTo(fakeFeaturedCategoriesWithDetails))
        }
}
