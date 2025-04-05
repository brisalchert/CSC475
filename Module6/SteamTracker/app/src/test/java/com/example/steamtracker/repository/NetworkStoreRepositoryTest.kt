package com.example.steamtracker.repository

import com.example.steamtracker.data.NetworkStoreRepository
import com.example.steamtracker.fake.FakeAppDetailsRequest
import com.example.steamtracker.fake.FakeFeaturedCategoriesRequest
import com.example.steamtracker.fake.FakeStoreApiService
import com.example.steamtracker.fake.FakeStoreSearchRequest
import com.example.steamtracker.room.dao.AppDetailsDao
import com.example.steamtracker.room.dao.StoreDao
import com.example.steamtracker.room.relations.FeaturedCategoryWithDetails
import com.example.steamtracker.utils.mapToAppInfoEntities
import com.example.steamtracker.utils.mapToFeaturedCategoryEntities
import com.example.steamtracker.utils.mapToSpotlightItemEntities
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class NetworkStoreRepositoryTest {
    private lateinit var repository: NetworkStoreRepository
    private lateinit var mockStoreDao: StoreDao
    private lateinit var mockAppDetailsDao: AppDetailsDao

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        mockStoreDao = Mockito.mock(StoreDao::class.java)
        mockAppDetailsDao = Mockito.mock(AppDetailsDao::class.java)
    }

    @Test
    fun networkStoreRepository_getAllFeaturedCategories_verifyFeaturedCategoriesCorrect() =
        runTest {
            val fakeFeaturedCategoriesWithDetails = FakeFeaturedCategoriesRequest.response.mapToFeaturedCategoryEntities().map {
                FeaturedCategoryWithDetails(
                    category = it,
                    appItems = FakeFeaturedCategoriesRequest.response.mapToAppInfoEntities(),
                    spotlightItems = FakeFeaturedCategoriesRequest.response.mapToSpotlightItemEntities()
                )
            }

            // Mock DAO behavior
            `when`(mockStoreDao.getAllFeaturedCategories()).thenReturn(
                flowOf(fakeFeaturedCategoriesWithDetails)
            )

            repository = NetworkStoreRepository(
                storeApiService = FakeStoreApiService(),
                storeDao = mockStoreDao,
                appDetailsDao = mockAppDetailsDao
            )

            assertEquals(
               fakeFeaturedCategoriesWithDetails,
                repository.allFeaturedCategories.first()
            )
        }

    @Test
    fun networkStoreRepository_getAppDetails_verifyAppDetailsCorrect() =
        runTest {
            val appId = 0

            // Mock DAO behavior
            `when`(mockAppDetailsDao.getAppDetails(appId)).thenReturn(null)

            repository = NetworkStoreRepository(
                storeApiService = FakeStoreApiService(),
                storeDao = mockStoreDao,
                appDetailsDao = mockAppDetailsDao
            )

            assertEquals(
                FakeAppDetailsRequest.response["$appId"]!!.appDetails,
                repository.getAppDetails(appId)
            )
        }

    @Test
    fun networkStoreRepository_getSearchResults_verifySearchResultsCorrect() =
        runTest {
            val query = ""

            repository = NetworkStoreRepository(
                storeApiService = FakeStoreApiService(),
                storeDao = mockStoreDao,
                appDetailsDao = mockAppDetailsDao
            )

            assertEquals(
                FakeStoreSearchRequest.response,
                repository.getSearchResults(query)
            )
        }
}
