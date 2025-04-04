package com.example.steamtracker.fake

import com.example.steamtracker.data.AppDetailsRepository
import com.example.steamtracker.model.AppDetails
import com.example.steamtracker.room.entities.AppDetailsEntity
import com.example.steamtracker.utils.toAppDetailsEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeNetworkAppDetailsRepository(
) : AppDetailsRepository {
    override val allAppDetails: Flow<List<AppDetailsEntity>> =
        flowOf(listOf(FakeAppDetailsRequest.response["0"]!!.appDetails!!.toAppDetailsEntity()))

    override suspend fun insertAppDetails(appDetails: AppDetails) {
    }

    override suspend fun getAppDetails(appId: Int): AppDetails? {
        return FakeAppDetailsRequest.response["0"]?.appDetails
    }

    override suspend fun deleteAppDetails(appId: Int) {
    }
}
