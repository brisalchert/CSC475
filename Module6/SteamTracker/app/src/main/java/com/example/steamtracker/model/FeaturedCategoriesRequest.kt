package com.example.steamtracker.model

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.annotations.SerializedName
import java.lang.reflect.Type

data class FeaturedCategoriesRequest(
    val spotlightCategories: Map<String, Any>? = mapOf(),
    val specials: RegularCategory? = RegularCategory(),
    @SerializedName(value = "coming_soon")
    val comingSoon: RegularCategory? = RegularCategory(),
    @SerializedName(value = "top_sellers")
    val topSellers: RegularCategory? = RegularCategory(),
    @SerializedName(value = "new_releases")
    val newReleases: RegularCategory? = RegularCategory(),
    val genres: StaticCategory? = StaticCategory(),
    val trailerslideshow: StaticCategory? = StaticCategory(),
    val status: Int = -1,
)

data class SpotlightCategory(
    val id: String = "ID",
    val name: String = "Name",
    val items: List<SpotlightItem>? = listOf()
)

data class RegularCategory(
    val id: String = "ID",
    val name: String = "Name",
    val items: List<AppInfo>? = listOf()
)

data class SpotlightItem(
    val name: String = "Name",
    @SerializedName(value = "header_image")
    val headerImage: String = "",
    val body: String = "",
    val url: String = ""
)

data class StaticCategory(
    val id: String = "ID",
    val name: String = "Name"
)

/**
 * Deserializer for the complex response provided by the featured categories request
 */
class FeaturedCategoriesDeserializer: JsonDeserializer<FeaturedCategoriesRequest> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): FeaturedCategoriesRequest {
        val jsonObject = json.asJsonObject
        val status = jsonObject["status"].asInt // Extract status from response

        // Extract explicitly named categories
        val specials = context?.deserialize<RegularCategory>(
            jsonObject["specials"],
            RegularCategory::class.java
        )
        val comingSoon = context?.deserialize<RegularCategory>(
            jsonObject["coming_soon"],
            RegularCategory::class.java
        )
        val topSellers = context?.deserialize<RegularCategory>(
            jsonObject["top_sellers"],
            RegularCategory::class.java
        )
        val newReleases = context?.deserialize<RegularCategory>(
            jsonObject["new_releases"],
            RegularCategory::class.java
        )
        val genres = context?.deserialize<StaticCategory>(
            jsonObject["genres"],
            StaticCategory::class.java
        )
        val trailerslideshow = context?.deserialize<StaticCategory>(
            jsonObject["trailerslideshow"],
            StaticCategory::class.java
        )

        // Extract spotlight categories dynamically
        val spotlightCategories = mutableMapOf<String, Any>()

        // Iterate over the key-value pairs in the JSON response
        for ((key, value) in jsonObject.entrySet()) {
            // Treat categories with name "Spotlights" as different objects
            if (key.toIntOrNull() != null) {
                val id = value.asJsonObject["id"].asString
                val name = value.asJsonObject["name"].asString

                if (name == "Spotlights") {
                    spotlightCategories[key] = context!!.deserialize(value, SpotlightCategory::class.java)
                } else {
                    spotlightCategories[key] = context!!.deserialize(value, RegularCategory::class.java)
                }
            }
        }

        return FeaturedCategoriesRequest(
            spotlightCategories,
            specials,
            comingSoon,
            topSellers,
            newReleases,
            genres,
            trailerslideshow,
            status
        )
    }
}
