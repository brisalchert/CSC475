package com.example.steamtracker.fake

import com.example.steamtracker.model.SteamSpyAppRequest

object FakeSteamSpyAppRequest {
    val response = HashMap<String, SteamSpyAppRequest>()

    init {
        response["gameId"] = SteamSpyAppRequest(
            tags = emptyMap()
        )
    }
}
