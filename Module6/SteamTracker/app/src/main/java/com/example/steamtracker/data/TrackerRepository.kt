package com.example.steamtracker.data

import com.example.steamtracker.model.GamePhotosRequestResult
import com.example.steamtracker.model.Screenshot
import com.example.steamtracker.network.TrackerApiService

interface TrackerRepository {
    suspend fun getGamePhotos(): List<Pair<String, List<Screenshot>>>
}

class NetworkTrackerRepository(
    private val trackerApiService: TrackerApiService,
    private val gameIdsToNames: Map<Int, String>
): TrackerRepository {
    /**
     * Returns pairs of game names and their respective lists of screenshots
     */
    override suspend fun getGamePhotos(): List<Pair<String, List<Screenshot>>> {
        val responses = ArrayList<Map<String, GamePhotosRequestResult>>()

        for (gameId: Int in gameIdsToNames.keys) {
            responses.add(trackerApiService.getGamePhotos(gameId))
        }

        // Reformat responses, extracting necessary information for the gallery
        return ArrayList<Pair<String, List<Screenshot>>>().apply {
            gameIdsToNames.values.zip(
                responses.map {it.values.firstOrNull()?.data?.screenshots?: emptyList()}
            ).forEach {
                    (game, photos) -> add(Pair(game, photos))
            }
        }
    }
}
