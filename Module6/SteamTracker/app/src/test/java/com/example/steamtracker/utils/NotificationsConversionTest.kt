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
import com.example.steamtracker.model.NewsItem
import com.example.steamtracker.model.NewsNotification
import com.example.steamtracker.model.PackageGroup
import com.example.steamtracker.model.Platforms
import com.example.steamtracker.model.PriceOverview
import com.example.steamtracker.model.Rating
import com.example.steamtracker.model.Recommendations
import com.example.steamtracker.model.ReleaseDate
import com.example.steamtracker.model.Screenshot
import com.example.steamtracker.model.SupportInfo
import com.example.steamtracker.model.SystemRequirements
import com.example.steamtracker.model.WishlistNotification
import com.example.steamtracker.room.entities.AppDetailsNotificationEntity
import com.example.steamtracker.room.entities.NewsItemNotificationEntity
import com.example.steamtracker.room.entities.NewsNotificationEntity
import com.example.steamtracker.room.entities.WishlistNotificationEntity
import com.example.steamtracker.room.relations.NewsNotificationWithDetails
import com.example.steamtracker.room.relations.WishlistNotificationWithDetails
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test

class NotificationsConversionTest {
    private lateinit var newsNotification: NewsNotification
    private lateinit var newsNotificationWithDetails: NewsNotificationWithDetails
    private lateinit var wishlistNotification: WishlistNotification
    private lateinit var wishlistNotificationWithDetails: WishlistNotificationWithDetails

    @Before
    fun setup() {
        newsNotification = NewsNotification(
            newPosts = listOf(
                NewsItem(
                    gid = "12",
                    title = "title",
                    url = "url",
                    isExternalUrl = false,
                    author = "author",
                    contents = "contents",
                    feedlabel = "feedlabel",
                    date = 1230L,
                    feedname = "feedname",
                    feedType = 23,
                    appid = 7
                )
            ),
            timestamp = 0L
        )

        newsNotificationWithDetails = NewsNotificationWithDetails(
            notification = NewsNotificationEntity(
                timestamp = 0L
            ),
            newPosts = listOf(
                NewsItemNotificationEntity(
                    gid = "12",
                    title = "title",
                    url = "url",
                    isExternalUrl = false,
                    author = "author",
                    contents = "contents",
                    feedlabel = "feedlabel",
                    date = 1230L,
                    feedname = "feedname",
                    feedType = 23,
                    appid = 7,
                    timestamp = 0L
                )
            )
        )

        wishlistNotification = WishlistNotification(
            newSales = listOf(
                AppDetails(
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
            ),
            timestamp = 0L
        )

        wishlistNotificationWithDetails = WishlistNotificationWithDetails(
            notification = WishlistNotificationEntity(
                timestamp = 0L
            ),
            newSales = listOf(
                AppDetailsNotificationEntity(
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
            )
        )
    }

    @Test
    fun newsNotificationWithDetails_toNewsNotification_verifyNewsNotification() {
        assertEquals(
            newsNotification,
            newsNotificationWithDetails.mapToNewsNotification()
        )
    }

    @Test
    fun wishlistNotificationWithDetails_toWishlistNotification_verifyWishlistNotification() {
        assertEquals(
            wishlistNotification,
            wishlistNotificationWithDetails.mapToWishlistNotification()
        )
    }
}
