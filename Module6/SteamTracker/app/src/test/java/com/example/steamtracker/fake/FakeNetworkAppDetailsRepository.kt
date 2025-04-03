package com.example.steamtracker.fake

import com.example.steamtracker.data.AppDetailsRepository
import com.example.steamtracker.model.AppDetails
import com.example.steamtracker.room.dao.AppDetailsDao
import com.example.steamtracker.room.entities.AppDetailsEntity
import com.example.steamtracker.utils.toAppDetails
import com.example.steamtracker.utils.toAppDetailsEntity
import kotlinx.coroutines.flow.Flow

class FakeNetworkAppDetailsRepository(
    private val appDetailsDao: AppDetailsDao
) : AppDetailsRepository {
    override val allAppDetails: Flow<List<AppDetailsEntity>> =
        appDetailsDao.getAllAppDetails()

    override suspend fun insertAppDetails(appDetails: AppDetails) {
        val appDetailsEntity = appDetails.toAppDetailsEntity()

        appDetailsDao.insertAppDetails(appDetailsEntity)
    }

    override suspend fun getAppDetails(appId: Int): AppDetails? {
        val entity = appDetailsDao.getAppDetails(appId)

        if (entity != null) {
            return entity.toAppDetails()
        }

        return null
    }

    override suspend fun deleteAppDetails(appId: Int) {
        appDetailsDao.deleteAppDetails(appId)
    }
}
