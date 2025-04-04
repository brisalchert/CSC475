package com.example.steamtracker

import com.example.steamtracker.data.NetworkStoreRepository
import com.example.steamtracker.fake.FakeAppDetailsRequest
import com.example.steamtracker.fake.FakeFeaturedCategoriesRequest
import com.example.steamtracker.fake.FakeStoreApiService
import com.example.steamtracker.fake.FakeStoreSearchRequest
import com.example.steamtracker.model.FeaturedCategoriesRequest
import com.example.steamtracker.model.RegularCategory
import com.example.steamtracker.model.SpotlightCategory
import com.example.steamtracker.room.dao.AppDetailsDao
import com.example.steamtracker.room.dao.StoreDao
import com.example.steamtracker.room.entities.AppInfoEntity
import com.example.steamtracker.room.entities.FeaturedCategoryEntity
import com.example.steamtracker.room.entities.SpotlightItemEntity
import com.example.steamtracker.room.relations.FeaturedCategoryWithDetails
import com.example.steamtracker.utils.toAppInfoEntityList
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
            val fakeFeaturedCategoriesWithDetails = mapRequestToEntities(
                FakeFeaturedCategoriesRequest.response
            ).map {
                FeaturedCategoryWithDetails(
                    category = it,
                    appItems = mapAppInfoToEntities(FakeFeaturedCategoriesRequest.response),
                    spotlightItems = mapSpotlightItemsToEntities(FakeFeaturedCategoriesRequest.response)
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

        request.spotlightCategories?.forEach { (key, value) ->
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
