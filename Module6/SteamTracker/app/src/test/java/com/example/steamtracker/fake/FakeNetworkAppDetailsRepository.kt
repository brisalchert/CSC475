package com.example.steamtracker.fake

import com.example.steamtracker.data.AppDetailsRepository
import com.example.steamtracker.model.AppDetails
import com.example.steamtracker.room.entities.AppDetailsEntity
import com.example.steamtracker.utils.toAppDetailsEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeNetworkAppDetailsRepository(
) : AppDetailsRepository {
    override val allAppDetails: Flow<List<AppDetailsEntity>> =
        flow {
            emit(listOf(FakeAppDetailsRequest.response["gameId"]!!.appDetails!!.toAppDetailsEntity()))
        }

    override suspend fun insertAppDetails(appDetails: AppDetails) {
    }

    override suspend fun getAppDetails(appId: Int): AppDetails? {
        return FakeAppDetailsRequest.response["gameId"]?.appDetails
    }

    override suspend fun deleteAppDetails(appId: Int) {
    }
}
