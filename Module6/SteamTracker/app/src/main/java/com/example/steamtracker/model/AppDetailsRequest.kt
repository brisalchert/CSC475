package com.example.steamtracker.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AppDetailsRequest(
    val success: Boolean,
    @SerialName(value = "data")
    val appDetails: AppDetails?
)

@Serializable
data class AppDetails(
    val type: String,
    val name: String,
    @SerialName(value = "steam_appid")
    val steamAppId: Int,
    @SerialName(value = "required_age")
    val requiredAge: String,
    @SerialName(value = "is_free")
    val isFree: Boolean,
    @SerialName(value = "controller_support")
    val controllerSupport: String? = null,
    val dlc: List<Int>? = null,
    @SerialName(value = "detailed_description")
    val detailedDescription: String,
    @SerialName(value = "about_the_game")
    val aboutTheGame: String,
    @SerialName(value = "short_description")
    val shortDescription: String,
    val fullgame: FullGame? = null,
    @SerialName(value = "supported_languages")
    val supportedLanguages: String,
    val reviews: String,
    @SerialName(value = "header_image")
    val headerImage: String,
    @SerialName(value = "capsule_image")
    val capsuleImage: String,
    @SerialName(value = "capsule_imagev5")
    val capsuleImageV5: String,
    val website: String?,
    @SerialName(value = "pc_requirements")
    val pcRequirements: SystemRequirements,
    @SerialName(value = "mac_requirements")
    val macRequirements: SystemRequirements,
    @SerialName(value = "linux_requirements")
    val linuxRequirements: SystemRequirements,
    @SerialName(value = "legal_notice")
    val legalNotice: String? = null,
    val developers: List<String>? = null,
    val publishers: List<String>,
    val demos: Demos? = null,
    @SerialName(value = "price_overview")
    val priceOverview: PriceOverview? = null,
    val packages: List<Int>,
    @SerialName(value = "package_groups")
    val packageGroups: List<PackageGroup>,
    val platforms: Platforms,
    val metacritic: MetaCritic? = null,
    val categories: List<Category>? = null,
    val genres: List<Genre>? = null,
    val screenshots: List<Screenshot>? = null,
    val movies: List<Movie>? = null,
    val recommendations: Recommendations? = null,
    val achievements: AchievementsContainer? = null,
    @SerialName(value = "release_date")
    val releaseDate: ReleaseDate,
    @SerialName(value = "support_info")
    val supportInfo: SupportInfo,
    val background: String,
    @SerialName(value = "background_raw")
    val backgroundRaw: String,
    @SerialName(value = "content_descriptors")
    val contentDescriptors: ContentDescriptors,
    val ratings: Map<String, Rating>
)

@Serializable
data class FullGame(
    val appid: Int?,
    val name: String = "Uninitialized"
)

@Serializable
data class SystemRequirements(
    val minimum: String,
    val recommended: String
)

@Serializable
data class Demos(
    val appid: Int,
    val description: String
)

@Serializable
data class PriceOverview(
    val currency: String,
    val initial: Int,
    val final: Int,
    @SerialName(value = "discount_percent")
    val discountPercent: Int,
    @SerialName(value = "initial_formatted")
    val initialFormatted: String,
    @SerialName(value = "final_formatted")
    val finalFormatted: String
)

@Serializable
data class PackageGroup(
    val name: String,
    val title: String,
    val description: String,
    @SerialName(value = "selection_text")
    val selectionText: String,
    @SerialName(value = "save_text")
    val saveText: String,
    @SerialName(value = "display_type")
    val displayType: Int,
    @SerialName(value = "is_recurring_subscription")
    val isRecurringSubscription: String,
    val subs: List<Package>
)

@Serializable
data class Package(
    val packageid: Int,
    @SerialName(value = "percent_savings_text")
    val percentSavingsText: String,
    @SerialName(value = "percent_savings")
    val percentSavings: Int,
    @SerialName(value = "option_text")
    val optionText: String,
    @SerialName(value = "option_description")
    val optionDescription: String,
    @SerialName(value = "can_get_free_license")
    val canGetFreeLicense: String,
    @SerialName(value = "is_free_license")
    val isFreeLicense: Boolean,
    @SerialName(value = "price_in_cents_with_discount")
    val priceInCentsWithDiscount: Int
)

@Serializable
data class Platforms(
    val windows: Boolean,
    val mac: Boolean,
    val linux: Boolean
)

@Serializable
data class MetaCritic(
    val score: Int,
    val url: String
)

@Serializable
data class Category(
    val id: Int,
    val description: String
)

@Serializable
data class Genre(
    val id: Int,
    val description: String
)

@Serializable
data class Screenshot(
    val id: Int,
    @SerialName(value = "path_thumbnail")
    val pathThumbnail: String,
    @SerialName(value = "path_full")
    val pathFull: String
)

@Serializable
data class Movie(
    val id: Int,
    val name: String,
    val thumbnail: String,
    val webm: WebM,
    val mp4: MP4,
    val highlight: Boolean
)

@Serializable
data class WebM(
    @SerialName(value = "480")
    val res480: String,
    val max: String
)

@Serializable
data class MP4(
    @SerialName(value = "480")
    val res480: String,
    val max: String
)

@Serializable
data class Recommendations(
    val total: Int
)

@Serializable
data class AchievementsContainer(
    val total: Int,
    val highlighted: List<Achievement>
)

@Serializable
data class Achievement(
    val name: String,
    val path: String
)

@Serializable
data class ReleaseDate(
    @SerialName(value = "coming_soon")
    val comingSoon: Boolean,
    val date: String
)

@Serializable
data class SupportInfo(
    val url: String,
    val email: String
)

@Serializable
data class ContentDescriptors(
    val ids: List<Int>,
    val notes: List<String>?
)

@Serializable
data class Rating(
    val rating: String,
    val descriptors: String? = null,
    @SerialName(value = "required_age")
    val requiredAge: String,
    @SerialName(value = "use_age_gate")
    val useAgeGate: String,
    @SerialName(value = "interactive_elements")
    val interactiveElements: String? = null
)
