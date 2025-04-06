package com.example.steamtracker.data

import com.example.steamtracker.model.AppDetails
import com.example.steamtracker.room.dao.AppDetailsDao
import com.example.steamtracker.room.entities.AppDetailsEntity
import com.example.steamtracker.utils.toAppDetails
import com.example.steamtracker.utils.toAppDetailsEntity
import kotlinx.coroutines.flow.Flow

interface AppDetailsRepository {
    val allAppDetails: Flow<List<AppDetailsEntity>>

    suspend fun insertAppDetails(appDetails: AppDetails)
    suspend fun getAppDetails(appId: Int): AppDetails?
    suspend fun deleteAppDetails(appId: Int)
}

class NetworkAppDetailsRepository(
    private val appDetailsDao: AppDetailsDao
): AppDetailsRepository {
    override val allAppDetails: Flow<List<AppDetailsEntity>> =
        appDetailsDao.getAllAppDetails()

    // Gson Type Converters allow for automatic conversion to database entities
    override suspend fun insertAppDetails(appDetails: AppDetails) {
        // Convert model class to entity class
        val appDetailsEntity = appDetails.toAppDetailsEntity()

        appDetailsDao.insertAppDetails(appDetailsEntity)
    }

    override suspend fun getAppDetails(appId: Int): AppDetails? {
        // Check the database, checking for ID alias
        val canonicalId = appDetailsDao.getCanonicalId(appId)

        val correctedAppId = canonicalId ?: appId

        val entity = appDetailsDao.getAppDetails(correctedAppId)

        if (entity != null) {
            return entity.toAppDetails()
        }

        return null
    }

    override suspend fun deleteAppDetails(appId: Int) {
        appDetailsDao.deleteAppDetails(appId)
    }
}
