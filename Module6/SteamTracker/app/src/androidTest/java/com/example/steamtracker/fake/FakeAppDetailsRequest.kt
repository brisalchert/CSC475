package com.example.steamtracker.fake

import com.example.steamtracker.model.AppDetails
import com.example.steamtracker.model.AppDetailsRequest
import com.example.steamtracker.model.Screenshot

object FakeAppDetailsRequest {
    val response = HashMap<String, AppDetailsRequest>()

    init {
        response["0"] = AppDetailsRequest(
            success = true,
            appDetails = AppDetails(
                screenshots = listOf(
                    Screenshot()
                )
            )
        )
    }
}
