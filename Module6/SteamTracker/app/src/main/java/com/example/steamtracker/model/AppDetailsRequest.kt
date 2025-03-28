package com.example.steamtracker.model

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.annotations.SerializedName
import java.lang.reflect.Type

data class AppDetailsRequest(
    val success: Boolean = false,
    @SerializedName(value = "data")
    val appDetails: AppDetails?
)

data class AppDetails(
    val type: String = "None",
    val name: String = "Name",
    @SerializedName(value = "steam_appid")
    val steamAppId: Int = 0,
    @SerializedName(value = "required_age")
    val requiredAge: Int = 0,
    @SerializedName(value = "is_free")
    val isFree: Boolean = false,
    @SerializedName(value = "controller_support")
    val controllerSupport: String? = null,
    val dlc: List<Int>? = null,
    @SerializedName(value = "detailed_description")
    val detailedDescription: String = "",
    @SerializedName(value = "about_the_game")
    val aboutTheGame: String = "",
    @SerializedName(value = "short_description")
    val shortDescription: String = "",
    val fullgame: FullGame? = null,
    @SerializedName(value = "supported_languages")
    val supportedLanguages: String = "",
    val reviews: String = "",
    @SerializedName(value = "header_image")
    val headerImage: String = "",
    @SerializedName(value = "capsule_image")
    val capsuleImage: String = "",
    @SerializedName(value = "capsule_imagev5")
    val capsuleImageV5: String = "",
    val website: String? = "",
    @SerializedName(value = "pc_requirements")
    val pcRequirements: SystemRequirements? = null,
    @SerializedName(value = "mac_requirements")
    val macRequirements: SystemRequirements? = null,
    @SerializedName(value = "linux_requirements")
    val linuxRequirements: SystemRequirements? = null,
    @SerializedName(value = "legal_notice")
    val legalNotice: String? = null,
    val developers: List<String>? = null,
    val publishers: List<String>? = null,
    val demos: List<Demos>? = null,
    @SerializedName(value = "price_overview")
    val priceOverview: PriceOverview? = null,
    val packages: List<Int> = listOf(),
    @SerializedName(value = "package_groups")
    val packageGroups: List<PackageGroup> = listOf(),
    val platforms: Platforms = Platforms(),
    val metacritic: MetaCritic? = null,
    val categories: List<Category>? = null,
    val genres: List<Genre>? = null,
    val screenshots: List<Screenshot>? = null,
    val movies: List<Movie>? = null,
    val recommendations: Recommendations? = null,
    val achievements: AchievementsContainer? = null,
    @SerializedName(value = "release_date")
    val releaseDate: ReleaseDate = ReleaseDate(),
    @SerializedName(value = "support_info")
    val supportInfo: SupportInfo = SupportInfo(),
    val background: String = "",
    @SerializedName(value = "background_raw")
    val backgroundRaw: String = "",
    @SerializedName(value = "content_descriptors")
    val contentDescriptors: ContentDescriptors = ContentDescriptors(),
    val ratings: Map<String, Rating>? = mapOf()
)

data class FullGame(
    val appid: Int? = 0,
    val name: String = "Uninitialized"
)

data class SystemRequirements(
    val minimum: String = "",
    val recommended: String = ""
)

data class Demos(
    val appid: Int = 0,
    val description: String = ""
)

data class PriceOverview(
    val currency: String = "USD",
    val initial: Int = 0,
    val final: Int = 0,
    @SerializedName(value = "discount_percent")
    val discountPercent: Int = 0,
    @SerializedName(value = "initial_formatted")
    val initialFormatted: String = "",
    @SerializedName(value = "final_formatted")
    val finalFormatted: String = ""
)

data class PackageGroup(
    val name: String = "",
    val title: String = "",
    val description: String = "",
    @SerializedName(value = "selection_text")
    val selectionText: String = "",
    @SerializedName(value = "save_text")
    val saveText: String = "",
    @SerializedName(value = "display_type")
    val displayType: Int = 0,
    @SerializedName(value = "is_recurring_subscription")
    val isRecurringSubscription: String = "",
    val subs: List<Package> = listOf()
)

data class Package(
    val packageid: Int = 0,
    @SerializedName(value = "percent_savings_text")
    val percentSavingsText: String = "",
    @SerializedName(value = "percent_savings")
    val percentSavings: Int = 0,
    @SerializedName(value = "option_text")
    val optionText: String = "",
    @SerializedName(value = "option_description")
    val optionDescription: String = "",
    @SerializedName(value = "can_get_free_license")
    val canGetFreeLicense: String = "",
    @SerializedName(value = "is_free_license")
    val isFreeLicense: Boolean = false,
    @SerializedName(value = "price_in_cents_with_discount")
    val priceInCentsWithDiscount: Int = 0
)

data class Platforms(
    val windows: Boolean = false,
    val mac: Boolean = false,
    val linux: Boolean = false
)

data class MetaCritic(
    val score: Int = 0,
    val url: String = ""
)

data class Category(
    val id: Int = 0,
    val description: String = ""
)

data class Genre(
    val id: Int = 0,
    val description: String = ""
)

data class Screenshot(
    val id: Int = 0,
    @SerializedName(value = "path_thumbnail")
    val pathThumbnail: String = "",
    @SerializedName(value = "path_full")
    val pathFull: String = ""
)

data class Movie(
    val id: Int = 0,
    val name: String = "",
    val thumbnail: String = "",
    val webm: WebM = WebM(),
    val mp4: MP4 = MP4(),
    val highlight: Boolean = false
)

data class WebM(
    @SerializedName(value = "480")
    val res480: String = "",
    val max: String = ""
)

data class MP4(
    @SerializedName(value = "480")
    val res480: String = "",
    val max: String = ""
)

data class Recommendations(
    val total: Int = 0
)

data class AchievementsContainer(
    val total: Int = 0,
    val highlighted: List<Achievement> = listOf()
)

data class Achievement(
    val name: String = "",
    val path: String = ""
)

data class ReleaseDate(
    @SerializedName(value = "coming_soon")
    val comingSoon: Boolean = false,
    val date: String = ""
)

data class SupportInfo(
    val url: String = "",
    val email: String = ""
)

data class ContentDescriptors(
    val ids: List<Int> = listOf(),
    val notes: String? = ""
)

data class Rating(
    val rating: String = "",
    val descriptors: String? = null,
    @SerializedName(value = "required_age")
    val requiredAge: String = "",
    @SerializedName(value = "use_age_gate")
    val useAgeGate: String = "",
    @SerializedName(value = "interactive_elements")
    val interactiveElements: String? = null
)

class RequiredAgeDeserializer : JsonDeserializer<Int> {
    override fun deserialize(json: JsonElement, typeOfT: Type?, context: JsonDeserializationContext?): Int? {
        return json.asString.toIntOrNull() ?: json.asInt
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
