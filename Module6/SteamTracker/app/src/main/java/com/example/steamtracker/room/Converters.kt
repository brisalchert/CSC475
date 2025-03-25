package com.example.steamtracker.room

import androidx.room.TypeConverter
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
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    private val gson = Gson()

    // Inline functions to reduce redundancy in type converters
    private inline fun <reified T> fromObject(obj: T?): String? = obj?.let { gson.toJson(it) }
    private inline fun <reified T> toObject(data: String?): T? =
        data?.takeIf { it.isNotEmpty() }?.let { gson.fromJson(it, T::class.java) }

    private inline fun <reified T> fromList(list: List<T>?): String = gson.toJson(list)
    private inline fun <reified T> toList(data: String?): List<T>? =
        data?.takeIf { it.isNotEmpty() }?.let { gson.fromJson(it, object : TypeToken<List<T>>() {}.type) }

    private inline fun <reified K, V> fromMap(map: Map<K, V>?): String? {
        return gson.toJson(map) // Convert the map to a JSON string
    }
    private inline fun <reified K, V> toMap(value: String?): Map<K, V>? {
        return if (value != null) {
            val mapType = object : TypeToken<Map<K, V>>() {}.type
            gson.fromJson(value, mapType) // Convert the JSON string back to a Map
        } else {
            null
        }
    }

    @TypeConverter
    fun fromPlatforms(platforms: Platforms?) = fromObject(platforms)
    @TypeConverter
    fun toPlatforms(data: String?) = toObject<Platforms>(data)

    @TypeConverter
    fun fromCategoryList(categories: List<Category>?) = fromList(categories)
    @TypeConverter
    fun toCategoryList(data: String?) = toList<Category>(data)

    @TypeConverter
    fun fromGenreList(genres: List<Genre>?) = fromList(genres)
    @TypeConverter
    fun toGenreList(data: String?) = toList<Genre>(data)

    @TypeConverter
    fun fromScreenshotList(screenshots: List<Screenshot>?) = fromList(screenshots)
    @TypeConverter
    fun toScreenshotList(data: String?) = toList<Screenshot>(data)

    @TypeConverter
    fun fromMovieList(movies: List<Movie>?) = fromList(movies)
    @TypeConverter
    fun toMovieList(data: String?) = toList<Movie>(data)

    @TypeConverter
    fun fromPriceOverview(priceOverview: PriceOverview?) = fromObject(priceOverview)
    @TypeConverter
    fun toPriceOverview(data: String?) = toObject<PriceOverview>(data)

    @TypeConverter
    fun fromFullGame(fullGame: FullGame?) = fromObject(fullGame)
    @TypeConverter
    fun toFullGame(data: String?) = toObject<FullGame>(data)

    @TypeConverter
    fun fromSystemRequirements(systemRequirements: SystemRequirements?) = fromObject(systemRequirements)
    @TypeConverter
    fun toSystemRequirements(data: String?) = toObject<SystemRequirements>(data)

    @TypeConverter
    fun fromDemosList(demos: List<Demos>?) = fromList<Demos>(demos)
    @TypeConverter
    fun toDemosList(data: String?) = toList<Demos>(data)

    @TypeConverter
    fun fromPackageGroupList(packageGroup: List<PackageGroup?>) = fromList(packageGroup)
    @TypeConverter
    fun toPackageGroupList(data: String?) = toList<PackageGroup>(data)

    @TypeConverter
    fun fromMetaCritic(metaCritic: MetaCritic?) = fromObject(metaCritic)
    @TypeConverter
    fun toMetaCritic(data: String?) = toObject<MetaCritic>(data)

    @TypeConverter
    fun fromRecommendations(recommendations: Recommendations?) = fromObject(recommendations)
    @TypeConverter
    fun toRecommendations(data: String?) = toObject<Recommendations>(data)

    @TypeConverter
    fun fromAchievementsContainer(achievementsContainer: AchievementsContainer?) = fromObject(achievementsContainer)
    @TypeConverter
    fun toAchievementsContainer(data: String?) = toObject<AchievementsContainer>(data)

    @TypeConverter
    fun fromReleaseDate(releaseDate: ReleaseDate?) = fromObject(releaseDate)
    @TypeConverter
    fun toReleaseDate(data: String?) = toObject<ReleaseDate>(data)

    @TypeConverter
    fun fromSupportInfo(supportInfo: SupportInfo?) = fromObject(supportInfo)
    @TypeConverter
    fun toSupportInfo(data: String?) = toObject<SupportInfo>(data)

    @TypeConverter
    fun fromContentDescriptors(contentDescriptors: ContentDescriptors?) = fromObject(contentDescriptors)
    @TypeConverter
    fun toContentDescriptors(data: String?) = toObject<ContentDescriptors>(data)

    @TypeConverter
    fun fromDlcList(dlc: List<Int>?) = fromList(dlc)
    @TypeConverter
    fun toDlcList(data: String?) = toList<Int>(data)

    @TypeConverter
    fun fromDevelopersList(developers: List<String>?) = fromList(developers)
    @TypeConverter
    fun toDevelopersList(data: String?) = toList<String>(data)

    @TypeConverter
    fun fromRatingsMap(ratings: Map<String, Rating>) = fromMap(ratings)
    @TypeConverter
    fun toRatingsMap(data: String?) = toMap<String, Rating>(data)
}
