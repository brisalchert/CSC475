package com.example.steamtracker.utils

import com.example.steamtracker.model.SteamSpyAppRequest
import com.example.steamtracker.room.entities.SteamSpyAppEntity
import com.example.steamtracker.room.entities.TagEntity
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test

class SteamSpyAppConversionTest {
    private lateinit var steamSpyAppRequest: SteamSpyAppRequest
    private lateinit var steamSpyAppEntity: SteamSpyAppEntity
    private lateinit var tagEntities: List<TagEntity>

    @Before
    fun setup() {
        steamSpyAppRequest = SteamSpyAppRequest(
            appid = 12,
            name = "name",
            developer = "developer",
            publisher = "publisher",
            scoreRank = "20",
            positive = 1230,
            negative = 39,
            userscore = 95,
            owners = "owners",
            averageForever = 12300,
            average2Weeks = 28342,
            medianForever = 9123,
            median2Weeks = 9425,
            price = "8999",
            initialprice = "9999",
            discount = "10",
            ccu = 12382,
            languages = "languages",
            genre = "genre",
            tags = mapOf("tag1" to 123, "tag2" to 456)
        )

        steamSpyAppEntity = SteamSpyAppEntity(
            appid = 12,
            name = "name",
            developer = "developer",
            publisher = "publisher",
            scoreRank = "20",
            positive = 1230,
            negative = 39,
            userscore = 95,
            owners = "owners",
            averageForever = 12300,
            average2Weeks = 28342,
            medianForever = 9123,
            median2Weeks = 9425,
            price = "8999",
            initialprice = "9999",
            discount = "10",
            ccu = 12382,
            languages = "languages",
            genre = "genre",
            lastUpdated = 0L
        )

        tagEntities = listOf(
            TagEntity(
                appid = 12,
                tagName = "tag1",
                tagCount = 123
            ),
            TagEntity(
                appid = 12,
                tagName = "tag2",
                tagCount = 456
            )
        )
    }

    @Test
    fun steamSpyAppRequest_toSteamSpyAppEntity_verifySteamSpyAppEntity() {
        assertEquals(
            steamSpyAppEntity,
            steamSpyAppRequest.toSteamSpyAppEntity().copy(lastUpdated = 0L)
        )
    }

    @Test
    fun steamSpyAppEntity_toSteamSpyAppRequest_verifySteamSpyAppRequest() {
        assertEquals(
            steamSpyAppRequest,
            steamSpyAppEntity.toSteamSpyAppRequest(tagEntities)
        )
    }
}
