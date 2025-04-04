package com.example.steamtracker.repository

import com.example.steamtracker.data.NetworkSpyRepository
import com.example.steamtracker.fake.FakeSpyApiService
import com.example.steamtracker.fake.FakeSteamSpyAppRequest
import com.example.steamtracker.room.dao.SpyDao
import com.example.steamtracker.room.entities.TagEntity
import com.example.steamtracker.room.relations.SteamSpyAppWithTags
import com.example.steamtracker.utils.toSteamSpyAppEntity
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class NetworkSpyRepositoryTest {
    private lateinit var repository: NetworkSpyRepository
    private lateinit var mockSpyDao: SpyDao

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        mockSpyDao = Mockito.mock(SpyDao::class.java)
    }

    @Test
    fun networkSpyRepository_getSpyAppInfo_verifyInfoCorrect() =
        runTest {
            val appId = 0

            // Mock DAO behavior
            `when`(mockSpyDao.getSpyInfo(appId)).thenReturn(null)

            repository = NetworkSpyRepository(
                spyApiService = FakeSpyApiService(),
                spyDao = mockSpyDao
            )

            assertEquals(
                FakeSteamSpyAppRequest.response["gameId"],
                repository.getSpyAppInfo(appId)
            )
        }

    @Test
    fun networkSpyRepository_getTopSales_verifyTopSalesCorrect() =
        runTest {
            val fakeSteamSpyAppRequest = FakeSteamSpyAppRequest.response["gameId"]
            val fakeSteamSpyAppsWithTags = SteamSpyAppWithTags(
                app = fakeSteamSpyAppRequest!!.toSteamSpyAppEntity(),
                tags = fakeSteamSpyAppRequest.tags?.map {
                    TagEntity(
                        appid = fakeSteamSpyAppRequest.appid,
                        tagName = it.key,
                        tagCount = it.value
                    )
                } ?: emptyList()
            )

            // Mock DAO behavior
            `when`(mockSpyDao.getTopSales()).thenReturn(flowOf(listOf(fakeSteamSpyAppsWithTags)))

            repository = NetworkSpyRepository(
                spyApiService = FakeSpyApiService(),
                spyDao = mockSpyDao
            )

            assertEquals(
                listOf(fakeSteamSpyAppsWithTags),
                repository.topSales.first()
            )
        }
}
