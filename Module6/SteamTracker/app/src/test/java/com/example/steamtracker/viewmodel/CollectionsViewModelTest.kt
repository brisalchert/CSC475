package com.example.steamtracker.viewmodel

import android.app.Application
import com.example.steamtracker.fake.FakeCollectionApp
import com.example.steamtracker.fake.FakeNetworkCollectionsRepository
import com.example.steamtracker.fake.FakeNetworkStoreRepository
import com.example.steamtracker.room.entities.CollectionAppEntity
import com.example.steamtracker.room.entities.CollectionEntity
import com.example.steamtracker.room.relations.CollectionWithApps
import com.example.steamtracker.rules.TestDispatcherRule
import com.example.steamtracker.ui.screens.CollectionsViewModel
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito

class CollectionsViewModelTest {
    @get:Rule
    val testDispatcher = TestDispatcherRule()

    private lateinit var mockApplication: Application
    private lateinit var collectionsViewModel: CollectionsViewModel

    @Before
    fun setup() {
        mockApplication = Mockito.mock(Application::class.java)

        collectionsViewModel = CollectionsViewModel(
            storeRepository = FakeNetworkStoreRepository(),
            collectionsRepository = FakeNetworkCollectionsRepository(),
            workManager = null
        )
    }

    @Test
    fun collectionsViewModel_getAllCollections_verifyCollectionsUiStateSuccess() =
        runTest {
            // View model calls getAllCollections() on init
            assertEquals(
                listOf(
                    CollectionWithApps(
                        collection = CollectionEntity(FakeCollectionApp.response.collectionName),
                        collectionAppsDetails = listOf(CollectionAppEntity(
                            collectionName = FakeCollectionApp.response.collectionName,
                            appid = FakeCollectionApp.response.appId,
                            index = FakeCollectionApp.response.index
                        ))
                    )
                ),
                collectionsViewModel.allCollections.first()
            )
        }

    @Test
    fun collectionsViewModel_isInCollection_verifyInCollection() =
        runTest {
            assertEquals(
                true,
                collectionsViewModel.isInCollection(
                    collectionName = FakeCollectionApp.response.collectionName,
                    appId = FakeCollectionApp.response.appId
                ).first()
            )
        }

    @Test
    fun collectionsViewModel_getCollectionContents_verifyCollectionContents() =
        runTest {
            assertEquals(
                listOf(FakeCollectionApp.response),
                collectionsViewModel.getCollectionContents(
                    collectionName = FakeCollectionApp.response.collectionName
                ).first()
            )
        }
}
