package com.example.steamtracker.data

import com.example.steamtracker.model.SteamSpyAppRequest
import com.example.steamtracker.network.SpyApiService
import com.example.steamtracker.room.dao.SpyDao
import com.example.steamtracker.room.entities.SteamSpyAppEntity
import com.example.steamtracker.room.entities.TagEntity
import com.example.steamtracker.room.relations.SteamSpyAppWithTags
import com.example.steamtracker.utils.isDataOutdated
import com.example.steamtracker.utils.toSteamSpyAppEntity
import com.example.steamtracker.utils.toSteamSpyAppRequest
import kotlinx.coroutines.flow.Flow

interface SpyRepository {
    val topSales: Flow<List<SteamSpyAppWithTags>>

    suspend fun refreshTopSales()
    suspend fun getSpyAppInfo(appId: Int): SteamSpyAppRequest
    suspend fun clearAllSpyApps()
}

class NetworkSpyRepository(
    private val spyApiService: SpyApiService,
    private val spyDao: SpyDao
): SpyRepository {
    override val topSales: Flow<List<SteamSpyAppWithTags>> =
        spyDao.getTopSales()

    override suspend fun refreshTopSales() {
        // Get the timestamp of the current sales data
        val lastUpdated = spyDao.getLastUpdatedTimestamp()

        // Check if the data is outdated (not from today)
        if (isDataOutdated(lastUpdated)) {
            // Get data from the API
            val response = spyApiService.getFirstPage()
                .map { it.value }
                .filter { it.discount != "0" }

            // Convert API response to Room database entities
            val spyEntities = mapRequestsToEntities(response)
            val tagEntities = mapTagsToEntities(response)

            // Insert into Room Database using transactions
            spyDao.insertAppInfoWithTags(spyEntities, tagEntities)
        }
    }

    override suspend fun getSpyAppInfo(appId: Int): SteamSpyAppRequest {
        // Check database first
        var databaseResponse = spyDao.getSpyInfo(appId)

        // Need to check tags, since "spyApiService.getFirstPage()" Does not
        // return tags, languages, or genres
        if (databaseResponse != null && databaseResponse.tags.isNotEmpty()
            && !isDataOutdated(databaseResponse.app.lastUpdated)) {
            val spyRequest = databaseResponse.app.toSteamSpyAppRequest(databaseResponse.tags)
            return spyRequest
        }

        val apiResponse = spyApiService.getAppDetails(appId)

        // Add response to the database
        val spyEntity = mapRequestsToEntities(listOf(apiResponse))
        val tagEntity = mapTagsToEntities(listOf(apiResponse))

        spyDao.insertAppInfoWithTags(spyEntity, tagEntity)

        return apiResponse
    }

    /**
     * Clears old data from the database
     */
    override suspend fun clearAllSpyApps() {
        spyDao.clearAllSpyApps()
    }

    /**
     * Maps a list of SteamSpyAppRequests to a list of database entities
     */
    private fun mapRequestsToEntities(requests: List<SteamSpyAppRequest>): List<SteamSpyAppEntity> {
        return requests.map { it.toSteamSpyAppEntity() }
    }

    /**
     * Maps the tags of a list of SteamSpyAppRequests to a list of lists of tags
     */
    private fun mapTagsToEntities(requests: List<SteamSpyAppRequest>): List<List<TagEntity>> {
        val tagEntityList = mutableListOf<List<TagEntity>>()

        requests.forEach { request ->
            val tagList = mutableListOf<TagEntity>()

            request.tags?.map { (key, value) ->
                tagList.add(
                    TagEntity(
                        appid = request.appid,
                        tagName = key,
                        tagCount = value
                    )
                )
            }

            tagEntityList.add(tagList)
        }

        return tagEntityList
    }
}
