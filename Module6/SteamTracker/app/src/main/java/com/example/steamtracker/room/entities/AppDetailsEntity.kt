package com.example.steamtracker.room.entities

import androidx.room.*
import com.example.steamtracker.model.AchievementsContainer
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
import com.example.steamtracker.room.Converters

// Store complex objects as JSON strings
@TypeConverters(Converters::class)
@Entity(tableName = "app_details")
data class AppDetailsEntity(
    @PrimaryKey val steamAppId: Int = 0,
    val type: String = "None",
    val name: String = "Name",
    val requiredAge: Int = 0,
    val isFree: Boolean = false,
    val controllerSupport: String? = null,
    val dlc: List<Int>? = null,
    val detailedDescription: String = "",
    val aboutTheGame: String = "",
    val shortDescription: String = "",
    val fullgame: FullGame? = null,
    val supportedLanguages: String = "",
    val reviews: String = "",
    val headerImage: String = "",
    val capsuleImage: String = "",
    val capsuleImageV5: String = "",
    val website: String? = "",
    val pcRequirements: SystemRequirements? = SystemRequirements(),
    val macRequirements: SystemRequirements? = SystemRequirements(),
    val linuxRequirements: SystemRequirements? = SystemRequirements(),
    val legalNotice: String? = null,
    val developers: List<String>? = null,
    val publishers: List<String>? = null,
    val demos: List<Demos>? = null,
    val priceOverview: PriceOverview? = null,
    val packages: List<Int> = listOf(),
    val packageGroups: List<PackageGroup> = listOf(),
    val platforms: Platforms = Platforms(),
    val metacritic: MetaCritic? = null,
    val categories: List<Category>? = null,
    val genres: List<Genre>? = null,
    val screenshots: List<Screenshot>? = null,
    val movies: List<Movie>? = null,
    val recommendations: Recommendations? = null,
    val achievements: AchievementsContainer? = null,
    val releaseDate: ReleaseDate = ReleaseDate(),
    val supportInfo: SupportInfo = SupportInfo(),
    val background: String = "",
    val backgroundRaw: String = "",
    val contentDescriptors: ContentDescriptors = ContentDescriptors(),
    val ratings: Map<String, Rating> = mapOf(),
    val lastUpdated: Long
)
