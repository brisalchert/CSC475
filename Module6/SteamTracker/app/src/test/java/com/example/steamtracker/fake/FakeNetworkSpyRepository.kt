package com.example.steamtracker.fake

import com.example.steamtracker.data.SpyRepository
import com.example.steamtracker.model.SteamSpyAppRequest
import com.example.steamtracker.room.entities.SteamSpyAppEntity
import com.example.steamtracker.room.entities.TagEntity
import com.example.steamtracker.room.relations.SteamSpyAppWithTags
import com.example.steamtracker.utils.toSteamSpyAppEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.collections.forEach

class FakeNetworkSpyRepository(
): SpyRepository {
    override val topSales: Flow<List<SteamSpyAppWithTags>> =
        flow {
            emit(listOf(
                SteamSpyAppWithTags(
                    app = FakeSteamSpyAppRequest.response["gameId"]!!.toSteamSpyAppEntity(),
                    tags = FakeSteamSpyAppRequest.response["gameId"]!!.tags?.map {
                        TagEntity(
                            appid = FakeSteamSpyAppRequest.response["gameId"]!!.appid,
                            tagName = it.key,
                            tagCount = it.value
                        )
                    } ?: emptyList()
                )
            ))
        }

    override suspend fun refreshTopSales() {
    }

    override suspend fun getSpyAppInfo(appId: Int): SteamSpyAppRequest {
        return FakeSteamSpyAppRequest.response["gameId"]!!
    }

    /**
     * Clears old data from the database
     */
    override suspend fun clearAllSpyApps() {
    }
}
