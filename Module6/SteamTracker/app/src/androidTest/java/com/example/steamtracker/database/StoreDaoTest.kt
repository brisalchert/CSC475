package com.example.steamtracker.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import com.example.steamtracker.TestDatabase
import com.example.steamtracker.fake.FakeFeaturedCategoriesRequest
import com.example.steamtracker.model.FeaturedCategoriesRequest
import com.example.steamtracker.model.RegularCategory
import com.example.steamtracker.model.SpotlightCategory
import com.example.steamtracker.room.dao.StoreDao
import com.example.steamtracker.room.entities.AppInfoEntity
import com.example.steamtracker.room.entities.FeaturedCategoryEntity
import com.example.steamtracker.room.entities.SpotlightItemEntity
import com.example.steamtracker.room.relations.FeaturedCategoryWithDetails
import com.example.steamtracker.utils.toAppInfoEntityList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.equalTo
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.IOException
import kotlin.collections.component1
import kotlin.collections.component2

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
            val fakeFeaturedCategoryEntities = mapRequestToEntities(
                FakeFeaturedCategoriesRequest.response
            )
            val fakeAppInfoEntities = mapAppInfoToEntities(FakeFeaturedCategoriesRequest.response)
            val fakeSpotlightItemEntities = mapSpotlightItemsToEntities(
                FakeFeaturedCategoriesRequest.response)
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

    /**
     * Maps the categories of a FeaturedCategoriesRequest to a list of database entities
     */
    private fun mapRequestToEntities(request: FeaturedCategoriesRequest): List<FeaturedCategoryEntity> {
        val entities = mutableListOf<FeaturedCategoryEntity>()

        request.specials?.let {
            entities.add(FeaturedCategoryEntity(it.id, it.name, "regular", request.status, System.currentTimeMillis()))
        }
        request.comingSoon?.let {
            entities.add(FeaturedCategoryEntity(it.id, it.name, "regular", request.status, System.currentTimeMillis()))
        }
        request.topSellers?.let {
            entities.add(FeaturedCategoryEntity(it.id, it.name, "regular", request.status, System.currentTimeMillis()))
        }
        request.newReleases?.let {
            entities.add(FeaturedCategoryEntity(it.id, it.name, "regular", request.status, System.currentTimeMillis()))
        }
        request.genres?.let {
            entities.add(FeaturedCategoryEntity(it.id, it.name, "static", request.status, System.currentTimeMillis()))
        }
        request.trailerslideshow?.let {
            entities.add(FeaturedCategoryEntity(it.id, it.name, "static", request.status, System.currentTimeMillis()))
        }

        request.spotlightCategories?.forEach { (_, value) ->
            if (value is SpotlightCategory) {
                entities.add(FeaturedCategoryEntity(value.id, value.name, "spotlight", request.status, System.currentTimeMillis()))
            } else if (value is RegularCategory) {
                entities.add(FeaturedCategoryEntity(value.id, value.name, "regular", request.status, System.currentTimeMillis()))
            }
        }

        return entities
    }

    /**
     * Maps the AppInfo objects of a FeaturedCategoriesRequest to a list of database entities
     */
    private fun mapAppInfoToEntities(request: FeaturedCategoriesRequest): List<AppInfoEntity> {
        return buildList {
            request.specials?.let { addAll(it.items?.toAppInfoEntityList(it.id) ?: emptyList()) }
            request.comingSoon?.let { addAll(it.items?.toAppInfoEntityList(it.id) ?: emptyList()) }
            request.topSellers?.let { addAll(it.items?.toAppInfoEntityList(it.id) ?: emptyList()) }
            request.newReleases?.let { addAll(it.items?.toAppInfoEntityList(it.id) ?: emptyList()) }
        }
    }

    /**
     * Maps the SpotLightItem objects of a FeaturedCategoriesRequest to a list of database entities
     */
    private fun mapSpotlightItemsToEntities(request: FeaturedCategoriesRequest): List<SpotlightItemEntity> {
        val spotlightEntities = mutableListOf<SpotlightItemEntity>()

        request.spotlightCategories?.forEach { (_, value) ->
            if (value is SpotlightCategory) {
                value.items?.forEach { item ->
                    spotlightEntities.add(
                        SpotlightItemEntity(
                            name = item.name,
                            categoryId = value.id,
                            headerImage = item.headerImage,
                            body = item.body,
                            url = item.url
                        )
                    )
                }
            }
        }

        return spotlightEntities
    }
}
