package com.example.steamtracker.model

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.annotations.SerializedName
import java.lang.reflect.Type
import kotlin.math.min

data class AppDetailsRequest(
    val success: Boolean,
    @SerializedName(value = "data")
    val appDetails: AppDetails?
)

data class AppDetails(
    val type: String,
    val name: String,
    @SerializedName(value = "steam_appid")
    val steamAppId: Int,
    @SerializedName(value = "required_age")
    val requiredAge: Int,
    @SerializedName(value = "is_free")
    val isFree: Boolean,
    @SerializedName(value = "controller_support")
    val controllerSupport: String? = null,
    val dlc: List<Int>? = null,
    @SerializedName(value = "detailed_description")
    val detailedDescription: String,
    @SerializedName(value = "about_the_game")
    val aboutTheGame: String,
    @SerializedName(value = "short_description")
    val shortDescription: String,
    val fullgame: FullGame? = null,
    @SerializedName(value = "supported_languages")
    val supportedLanguages: String,
    val reviews: String,
    @SerializedName(value = "header_image")
    val headerImage: String,
    @SerializedName(value = "capsule_image")
    val capsuleImage: String,
    @SerializedName(value = "capsule_imagev5")
    val capsuleImageV5: String,
    val website: String?,
    @SerializedName(value = "pc_requirements")
    val pcRequirements: SystemRequirements,
    @SerializedName(value = "mac_requirements")
    val macRequirements: SystemRequirements?,
    @SerializedName(value = "linux_requirements")
    val linuxRequirements: SystemRequirements?,
    @SerializedName(value = "legal_notice")
    val legalNotice: String? = null,
    val developers: List<String>? = null,
    val publishers: List<String>,
    val demos: List<Demos>? = null,
    @SerializedName(value = "price_overview")
    val priceOverview: PriceOverview? = null,
    val packages: List<Int>,
    @SerializedName(value = "package_groups")
    val packageGroups: List<PackageGroup>,
    val platforms: Platforms,
    val metacritic: MetaCritic? = null,
    val categories: List<Category>? = null,
    val genres: List<Genre>? = null,
    val screenshots: List<Screenshot>? = null,
    val movies: List<Movie>? = null,
    val recommendations: Recommendations? = null,
    val achievements: AchievementsContainer? = null,
    @SerializedName(value = "release_date")
    val releaseDate: ReleaseDate,
    @SerializedName(value = "support_info")
    val supportInfo: SupportInfo,
    val background: String,
    @SerializedName(value = "background_raw")
    val backgroundRaw: String,
    @SerializedName(value = "content_descriptors")
    val contentDescriptors: ContentDescriptors,
    val ratings: Map<String, Rating>
)

data class FullGame(
    val appid: Int?,
    val name: String = "Uninitialized"
)

data class SystemRequirements(
    val minimum: String,
    val recommended: String
)

data class Demos(
    val appid: Int,
    val description: String
)

data class PriceOverview(
    val currency: String,
    val initial: Int,
    val final: Int,
    @SerializedName(value = "discount_percent")
    val discountPercent: Int,
    @SerializedName(value = "initial_formatted")
    val initialFormatted: String,
    @SerializedName(value = "final_formatted")
    val finalFormatted: String
)

data class PackageGroup(
    val name: String,
    val title: String,
    val description: String,
    @SerializedName(value = "selection_text")
    val selectionText: String,
    @SerializedName(value = "save_text")
    val saveText: String,
    @SerializedName(value = "display_type")
    val displayType: Int,
    @SerializedName(value = "is_recurring_subscription")
    val isRecurringSubscription: String,
    val subs: List<Package>
)

data class Package(
    val packageid: Int,
    @SerializedName(value = "percent_savings_text")
    val percentSavingsText: String,
    @SerializedName(value = "percent_savings")
    val percentSavings: Int,
    @SerializedName(value = "option_text")
    val optionText: String,
    @SerializedName(value = "option_description")
    val optionDescription: String,
    @SerializedName(value = "can_get_free_license")
    val canGetFreeLicense: String,
    @SerializedName(value = "is_free_license")
    val isFreeLicense: Boolean,
    @SerializedName(value = "price_in_cents_with_discount")
    val priceInCentsWithDiscount: Int
)

data class Platforms(
    val windows: Boolean,
    val mac: Boolean,
    val linux: Boolean
)

data class MetaCritic(
    val score: Int,
    val url: String
)

data class Category(
    val id: Int,
    val description: String
)

data class Genre(
    val id: Int,
    val description: String
)

data class Screenshot(
    val id: Int,
    @SerializedName(value = "path_thumbnail")
    val pathThumbnail: String,
    @SerializedName(value = "path_full")
    val pathFull: String
)

data class Movie(
    val id: Int,
    val name: String,
    val thumbnail: String,
    val webm: WebM,
    val mp4: MP4,
    val highlight: Boolean
)

data class WebM(
    @SerializedName(value = "480")
    val res480: String,
    val max: String
)

data class MP4(
    @SerializedName(value = "480")
    val res480: String,
    val max: String
)

data class Recommendations(
    val total: Int
)

data class AchievementsContainer(
    val total: Int,
    val highlighted: List<Achievement>
)

data class Achievement(
    val name: String,
    val path: String
)

data class ReleaseDate(
    @SerializedName(value = "coming_soon")
    val comingSoon: Boolean,
    val date: String
)

data class SupportInfo(
    val url: String,
    val email: String
)

data class ContentDescriptors(
    val ids: List<Int>,
    val notes: String?
)

data class Rating(
    val rating: String,
    val descriptors: String? = null,
    @SerializedName(value = "required_age")
    val requiredAge: String,
    @SerializedName(value = "use_age_gate")
    val useAgeGate: String,
    @SerializedName(value = "interactive_elements")
    val interactiveElements: String? = null
)

class RequiredAgeDeserializer: JsonDeserializer<Int> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Int? {
        return when {
            json.isJsonPrimitive -> {
                try {
                    json.asInt
                } catch (e: NumberFormatException) {
                    json.asString.toIntOrNull()
                }
            }
            else -> null
        }
    }
}

class SystemRequirementsDeserializer: JsonDeserializer<SystemRequirements?> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): SystemRequirements? {
        return if (json.isJsonObject) {
            val jsonObject = json.asJsonObject

            val minimum = jsonObject.get("minimum")?.asString ?: ""
            val recommended = jsonObject.get("recommended")?.asString ?: ""

            SystemRequirements(minimum, recommended)
        } else {
            null
        }
    }
}
