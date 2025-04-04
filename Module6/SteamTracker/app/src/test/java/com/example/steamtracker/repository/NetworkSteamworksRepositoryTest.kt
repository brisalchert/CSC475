package com.example.steamtracker.repository

import com.example.steamtracker.data.NetworkSteamworksRepository
import com.example.steamtracker.fake.FakeAppNewsRequest
import com.example.steamtracker.fake.FakeSteamworksApiService
import com.example.steamtracker.room.dao.NewsAppsDao
import com.example.steamtracker.room.dao.SteamworksDao
import com.example.steamtracker.room.entities.AppNewsEntity
import com.example.steamtracker.room.entities.AppNewsRequestEntity
import com.example.steamtracker.room.relations.AppNewsWithDetails
import com.example.steamtracker.room.relations.AppNewsWithItems
import com.example.steamtracker.utils.toNewsItemEntity
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class NetworkSteamworksRepositoryTest {
    private lateinit var repository: NetworkSteamworksRepository
    private lateinit var mockSteamworksDao: SteamworksDao
    private lateinit var mockNewsAppsDao: NewsAppsDao

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        mockSteamworksDao = Mockito.mock(SteamworksDao::class.java)
        mockNewsAppsDao = Mockito.mock(NewsAppsDao::class.java)
    }

    @Test
    fun networkSteamworksRepository_getAllAppNews_verifyNewsCorrect() =
        runTest {
            val fakeAppNewsWithDetails = AppNewsWithDetails(
                request = AppNewsRequestEntity(
                    appid = FakeAppNewsRequest.response.appnews.appid,
                    lastUpdated = 0L
                ),
                appNewsWithItems = AppNewsWithItems(
                    appnews = AppNewsEntity(
                        appid = FakeAppNewsRequest.response.appnews.appid
                    ),
                    newsitems = FakeAppNewsRequest.response.appnews.newsitems.map {
                        it.toNewsItemEntity()
                    }
                )
            )

            // Mock DAO behavior
            `when`(mockSteamworksDao.getAllAppNews()).thenReturn(
                flowOf(listOf(fakeAppNewsWithDetails))
            )

            repository = NetworkSteamworksRepository(
                steamworksApiService = FakeSteamworksApiService(),
                steamworksDao = mockSteamworksDao,
                newsAppsDao = mockNewsAppsDao
            )

            assertEquals(
                listOf(fakeAppNewsWithDetails),
                repository.getAllAppNews()
            )
        }

    @Test
    fun networkSteamworksRepository_getNewsByGid_verifyNewsItemCorrect() =
        runTest {
            val gid = "0"
            val fakeNewsItemEntity = FakeAppNewsRequest.response.appnews.newsitems.first().toNewsItemEntity()

            // Mock DAO behavior
            `when`(mockSteamworksDao.getNewsByGid(gid)).thenReturn(
                fakeNewsItemEntity
            )

            repository = NetworkSteamworksRepository(
                steamworksApiService = FakeSteamworksApiService(),
                steamworksDao = mockSteamworksDao,
                newsAppsDao = mockNewsAppsDao
            )

            assertEquals(
                fakeNewsItemEntity,
                repository.getNewsByGid(gid)
            )
        }

    @Test
    fun networkSteamworksRepository_getNewsAppsIds_verifyNewsAppsIdsCorrect() =
        runTest {
            val appId = 0

            // Mock DAO behavior
            `when`(mockNewsAppsDao.getNewsAppIds()).thenReturn(flowOf(listOf(appId)))

            repository = NetworkSteamworksRepository(
                steamworksApiService = FakeSteamworksApiService(),
                steamworksDao = mockSteamworksDao,
                newsAppsDao = mockNewsAppsDao
            )

            assertEquals(
                listOf(appId),
                repository.newsApps.first()
            )
        }
}
