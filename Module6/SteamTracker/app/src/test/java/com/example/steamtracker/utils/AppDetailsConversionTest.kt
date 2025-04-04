package com.example.steamtracker.utils

import com.example.steamtracker.model.AchievementsContainer
import com.example.steamtracker.model.AppDetails
import com.example.steamtracker.model.Category
import com.example.steamtracker.model.ContentDescriptors
import com.example.steamtracker.model.Demos
import com.example.steamtracker.model.FullGame
import com.example.steamtracker.model.Genre
import com.example.steamtracker.model.MetaCritic
import com.example.steamtracker.model.Movie
import com.example.steamtracker.model.PackageGroup
import com.example.steamtracker.model.Platforms
import com.example.steamtracker.model.PriceOverview
import com.example.steamtracker.model.Rating
import com.example.steamtracker.model.Recommendations
import com.example.steamtracker.model.ReleaseDate
import com.example.steamtracker.model.Screenshot
import com.example.steamtracker.model.SupportInfo
import com.example.steamtracker.model.SystemRequirements
import com.example.steamtracker.room.entities.AppDetailsEntity
import com.example.steamtracker.room.entities.AppDetailsNotificationEntity
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test

class AppDetailsConversionTest {
    private lateinit var appDetails: AppDetails
    private lateinit var appDetailsEntity: AppDetailsEntity
    private lateinit var appDetailsNotificationEntity: AppDetailsNotificationEntity

    @Before
    fun setup() {
        appDetails = AppDetails(
            steamAppId = 12,
            type = "type",
            name = "name",
            requiredAge = 14,
            isFree = true,
            controllerSupport = "support",
            dlc = listOf(1, 2, 3),
            detailedDescription = "Detailed description",
            aboutTheGame = "About the game",
            shortDescription = "Short description",
            fullgame = FullGame(),
            supportedLanguages = "Languages",
            reviews = "Reviews",
            headerImage = "Header image",
            capsuleImage = "Capsule Image",
            capsuleImageV5 = "Capsule ImageV5",
            website = "Website",
            pcRequirements = SystemRequirements(),
            macRequirements = SystemRequirements(),
            linuxRequirements = SystemRequirements(),
            legalNotice = "Legal notice",
            developers = listOf("developer"),
            publishers = listOf("publisher"),
            demos = listOf(Demos()),
            priceOverview = PriceOverview(),
            packages = listOf(1, 2),
            packageGroups = listOf(PackageGroup()),
            platforms = Platforms(),
            metacritic = MetaCritic(),
            categories = listOf(Category()),
            genres = listOf(Genre()),
            screenshots = listOf(Screenshot()),
            movies = listOf(Movie()),
            recommendations = Recommendations(),
            achievements = AchievementsContainer(),
            releaseDate = ReleaseDate(),
            supportInfo = SupportInfo(),
            background = "background",
            backgroundRaw = "background raw",
            contentDescriptors = ContentDescriptors(),
            ratings = mapOf("Rating" to Rating())
        )
        appDetailsEntity = AppDetailsEntity(
            steamAppId = 12,
            type = "type",
            name = "name",
            requiredAge = 14,
            isFree = true,
            controllerSupport = "support",
            dlc = listOf(1, 2, 3),
            detailedDescription = "Detailed description",
            aboutTheGame = "About the game",
            shortDescription = "Short description",
            fullgame = FullGame(),
            supportedLanguages = "Languages",
            reviews = "Reviews",
            headerImage = "Header image",
            capsuleImage = "Capsule Image",
            capsuleImageV5 = "Capsule ImageV5",
            website = "Website",
            pcRequirements = SystemRequirements(),
            macRequirements = SystemRequirements(),
            linuxRequirements = SystemRequirements(),
            legalNotice = "Legal notice",
            developers = listOf("developer"),
            publishers = listOf("publisher"),
            demos = listOf(Demos()),
            priceOverview = PriceOverview(),
            packages = listOf(1, 2),
            packageGroups = listOf(PackageGroup()),
            platforms = Platforms(),
            metacritic = MetaCritic(),
            categories = listOf(Category()),
            genres = listOf(Genre()),
            screenshots = listOf(Screenshot()),
            movies = listOf(Movie()),
            recommendations = Recommendations(),
            achievements = AchievementsContainer(),
            releaseDate = ReleaseDate(),
            supportInfo = SupportInfo(),
            background = "background",
            backgroundRaw = "background raw",
            contentDescriptors = ContentDescriptors(),
            ratings = mapOf("Rating" to Rating()),
            lastUpdated = 0L
        )

        appDetailsNotificationEntity = AppDetailsNotificationEntity(
            steamAppId = 12,
            type = "type",
            name = "name",
            requiredAge = 14,
            isFree = true,
            controllerSupport = "support",
            dlc = listOf(1, 2, 3),
            detailedDescription = "Detailed description",
            aboutTheGame = "About the game",
            shortDescription = "Short description",
            fullgame = FullGame(),
            supportedLanguages = "Languages",
            reviews = "Reviews",
            headerImage = "Header image",
            capsuleImage = "Capsule Image",
            capsuleImageV5 = "Capsule ImageV5",
            website = "Website",
            pcRequirements = SystemRequirements(),
            macRequirements = SystemRequirements(),
            linuxRequirements = SystemRequirements(),
            legalNotice = "Legal notice",
            developers = listOf("developer"),
            publishers = listOf("publisher"),
            demos = listOf(Demos()),
            priceOverview = PriceOverview(),
            packages = listOf(1, 2),
            packageGroups = listOf(PackageGroup()),
            platforms = Platforms(),
            metacritic = MetaCritic(),
            categories = listOf(Category()),
            genres = listOf(Genre()),
            screenshots = listOf(Screenshot()),
            movies = listOf(Movie()),
            recommendations = Recommendations(),
            achievements = AchievementsContainer(),
            releaseDate = ReleaseDate(),
            supportInfo = SupportInfo(),
            background = "background",
            backgroundRaw = "background raw",
            contentDescriptors = ContentDescriptors(),
            ratings = mapOf("Rating" to Rating()),
            lastUpdated = 0L,
            timestamp = 0L
        )
    }

    @Test
    fun appDetails_toAppDetailsEntity_verifyAppDetailsEntity() {
        assertEquals(
            appDetailsEntity,
            appDetails.toAppDetailsEntity().copy(lastUpdated = 0L)
        )
    }

    @Test
    fun appDetailsEntity_toAppDetails_verifyAppDetails() {
        assertEquals(
            appDetails,
            appDetailsEntity.toAppDetails()
        )
    }
}
