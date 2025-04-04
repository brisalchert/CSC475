package com.example.steamtracker.repository

import com.example.steamtracker.data.NetworkAppDetailsRepository
import com.example.steamtracker.fake.FakeAppDetailsRequest
import com.example.steamtracker.room.dao.AppDetailsDao
import com.example.steamtracker.utils.toAppDetails
import com.example.steamtracker.utils.toAppDetailsEntity
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class NetworkAppDetailsRepositoryTest {
    private lateinit var mockAppDetailsDao: AppDetailsDao
    private lateinit var repository: NetworkAppDetailsRepository

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        mockAppDetailsDao = Mockito.mock(AppDetailsDao::class.java)
    }

    @Test
    fun networkAppDetailsRepository_getAppDetails_verifyAppDetails() =
        runTest {
            val fakeGameId = 0
            val fakeAppDetails = FakeAppDetailsRequest.response["$fakeGameId"]?.appDetails
            val fakeAppDetailsEntity = fakeAppDetails?.toAppDetailsEntity()

            // Mock DAO behavior
            `when`(mockAppDetailsDao.getAppDetails(fakeGameId)).thenReturn(fakeAppDetailsEntity)

            repository = NetworkAppDetailsRepository(
                appDetailsDao = mockAppDetailsDao
            )

            assertEquals(
                fakeAppDetails,
                repository.getAppDetails(fakeGameId)
            )
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun networkAppDetailsRepository_allAppDetails_verifyAppDetailsList() =
        runTest {
            val appId = 0
            val fakeAppDetailsFlow = flowOf(
                listOf(FakeAppDetailsRequest.response["$appId"]!!.appDetails!!.toAppDetailsEntity())
            )

            // Mock DAO behavior
            `when`(mockAppDetailsDao.getAllAppDetails()).thenReturn(fakeAppDetailsFlow)

            repository = NetworkAppDetailsRepository(
                appDetailsDao = mockAppDetailsDao
            )

            assertEquals(
                listOf(FakeAppDetailsRequest.response["$appId"]!!.appDetails),
                repository.allAppDetails.first().map { it.toAppDetails() }
            )
        }
}
