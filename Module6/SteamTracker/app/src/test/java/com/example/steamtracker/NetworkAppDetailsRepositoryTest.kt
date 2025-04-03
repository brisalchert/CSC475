package com.example.steamtracker

import com.example.steamtracker.data.NetworkAppDetailsRepository
import com.example.steamtracker.fake.FakeAppDetailsRequest
import com.example.steamtracker.room.dao.AppDetailsDao
import com.example.steamtracker.utils.toAppDetailsEntity
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class NetworkAppDetailsRepositoryTest {
    private lateinit var mockAppDetailsDao: AppDetailsDao

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        mockAppDetailsDao = Mockito.mock(AppDetailsDao::class.java)
    }

    @Test
    fun networkAppDetailsRepository_getAppDetails_verifyAppDetails() =
        runTest {
            val fakeGameId = 0
            val fakeAppDetails = FakeAppDetailsRequest.response["gameId"]?.appDetails
            val fakeAppDetailsEntity = fakeAppDetails?.toAppDetailsEntity()

            // Mock DAO behavior
            `when`(mockAppDetailsDao.getAppDetails(fakeGameId)).thenReturn(fakeAppDetailsEntity)

            val repository = NetworkAppDetailsRepository(
                appDetailsDao = mockAppDetailsDao
            )

            assertEquals(
                fakeAppDetails,
                repository.getAppDetails(fakeGameId)
            )
        }
}
