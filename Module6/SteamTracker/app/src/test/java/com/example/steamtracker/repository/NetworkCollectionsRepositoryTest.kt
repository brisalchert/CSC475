package com.example.steamtracker.repository

import com.example.steamtracker.data.NetworkCollectionsRepository
import com.example.steamtracker.fake.FakeCollectionApp
import com.example.steamtracker.room.dao.CollectionsDao
import com.example.steamtracker.room.entities.CollectionAppEntity
import com.example.steamtracker.room.entities.CollectionEntity
import com.example.steamtracker.room.relations.CollectionWithApps
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class NetworkCollectionsRepositoryTest {
    private lateinit var mockCollectionsDao: CollectionsDao
    private lateinit var repository: NetworkCollectionsRepository

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        mockCollectionsDao = Mockito.mock(CollectionsDao::class.java)
    }

    @Test
    fun networkCollectionsRepository_getCollection_verifyCollectionCorrect() =
        runTest {
            val fakeCollectionName = FakeCollectionApp.response.collectionName
            val fakeCollectionWithApps = CollectionWithApps(
                collection = CollectionEntity(fakeCollectionName),
                collectionAppsDetails = listOf(CollectionAppEntity(
                    collectionName = fakeCollectionName,
                    appid = FakeCollectionApp.response.appId,
                    index = FakeCollectionApp.response.index
                ))
            )
            val fakeCollectionApps = listOf(FakeCollectionApp.response)

            // Mock DAO behavior
            `when`(mockCollectionsDao.getCollectionByName(fakeCollectionName)).thenReturn(
                fakeCollectionWithApps
            )

            repository = NetworkCollectionsRepository(
                collectionsDao = mockCollectionsDao
            )

            assertEquals(
                fakeCollectionApps,
                repository.getCollection(fakeCollectionName)
            )
        }
}
