package com.example.steamtracker.utils

import com.example.steamtracker.model.AppInfo
import com.example.steamtracker.model.FeaturedCategoriesRequest
import com.example.steamtracker.model.RegularCategory
import com.example.steamtracker.model.SpotlightCategory
import com.example.steamtracker.model.SpotlightItem
import com.example.steamtracker.model.StaticCategory
import com.example.steamtracker.room.entities.AppInfoEntity
import com.example.steamtracker.room.entities.FeaturedCategoryEntity
import com.example.steamtracker.room.entities.SpotlightItemEntity
import com.example.steamtracker.room.relations.FeaturedCategoryWithDetails
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test

class FeaturedCategoriesConversionTest {
    private lateinit var featuredCategoriesRequest: FeaturedCategoriesRequest
    private lateinit var featuredCategoriesEntities: List<FeaturedCategoryEntity>
    private lateinit var appInfoEntities: List<AppInfoEntity>
    private lateinit var spotlightItemEntities: List<SpotlightItemEntity>
    private lateinit var featuredCategoriesWithDetails: List<FeaturedCategoryWithDetails>

    @Before
    fun setup() {
        featuredCategoriesRequest = FeaturedCategoriesRequest(
            spotlightCategories = mapOf(
                "spotlight" to SpotlightCategory(
                    id = "spotlight",
                    name = "spotlight name",
                    items = listOf(
                        SpotlightItem(
                            name = "Name",
                            headerImage = "header image",
                            body = "body",
                            url = "url"
                        )
                    )
                )
            ),
            specials = RegularCategory(
                id = "1",
                name = "Specials",
                items = listOf(
                    AppInfo(),
                    AppInfo()
                )
            ),
            comingSoon = RegularCategory(
                id = "2",
                name = "Coming Soon",
                items = listOf(
                    AppInfo(),
                    AppInfo()
                )
            ),
            topSellers = RegularCategory(
                id = "3",
                name = "Top Sellers",
                items = listOf(
                    AppInfo(),
                    AppInfo()
                )
            ),
            newReleases = RegularCategory(
                id = "4",
                name = "New Releases",
                items = listOf(
                    AppInfo(),
                    AppInfo()
                )
            ),
            genres = StaticCategory(
                id = "5",
                name = "Genres"
            ),
            trailerslideshow = StaticCategory(
                id = "6",
                name = "Trailer Slideshow"
            ),
            status = 1
        )

        featuredCategoriesEntities = listOf(
            FeaturedCategoryEntity(
                id = "1",
                name = "Specials",
                type = "regular",
                status = 1,
                lastUpdated = 0L
            ),
            FeaturedCategoryEntity(
                id = "2",
                name = "Coming Soon",
                type = "regular",
                status = 1,
                lastUpdated = 0L
            ),
            FeaturedCategoryEntity(
                id = "3",
                name = "Top Sellers",
                type = "regular",
                status = 1,
                lastUpdated = 0L
            ),
            FeaturedCategoryEntity(
                id = "4",
                name = "New Releases",
                type = "regular",
                status = 1,
                lastUpdated = 0L
            ),
            FeaturedCategoryEntity(
                id = "5",
                name = "Genres",
                type = "static",
                status = 1,
                lastUpdated = 0L
            ),
            FeaturedCategoryEntity(
                id = "6",
                name = "Trailer Slideshow",
                type = "static",
                status = 1,
                lastUpdated = 0L
            ),
            FeaturedCategoryEntity(
                id = "spotlight",
                name = "spotlight name",
                type = "spotlight",
                status = 1,
                lastUpdated = 0L
            )
        )

        appInfoEntities = listOf(
            AppInfo().toAppInfoEntity("1"),
            AppInfo().toAppInfoEntity("1"),
            AppInfo().toAppInfoEntity("2"),
            AppInfo().toAppInfoEntity("2"),
            AppInfo().toAppInfoEntity("3"),
            AppInfo().toAppInfoEntity("3"),
            AppInfo().toAppInfoEntity("4"),
            AppInfo().toAppInfoEntity("4")
        )

        spotlightItemEntities = listOf(
            SpotlightItemEntity(
                name = "Name",
                categoryId = "spotlight",
                headerImage = "header image",
                body = "body",
                url = "url"
            )
        )

        featuredCategoriesWithDetails = featuredCategoriesEntities.map { category ->
            FeaturedCategoryWithDetails(
                category = category,
                appItems = appInfoEntities.filter { it.categoryId == category.id },
                spotlightItems = spotlightItemEntities
            )
        }
    }

    @Test
    fun featuredCategoriesRequest_toFeaturedCategoryEntities_verifyFeaturedCategoryEntities() {
        assertEquals(
            featuredCategoriesEntities,
            featuredCategoriesRequest.mapToFeaturedCategoryEntities().map {
                it.copy(lastUpdated = 0L)
            }
        )
    }

    @Test
    fun featuredCategoriesRequest_toAppInfoEntities_verifyAppInfoEntities() {
        assertEquals(
            appInfoEntities,
            featuredCategoriesRequest.mapToAppInfoEntities()
        )
    }

    @Test
    fun featuredCategoriesRequest_toSpotlightItemEntities_verifySpotlightItemEntities() {
        assertEquals(
            spotlightItemEntities,
            featuredCategoriesRequest.mapToSpotlightItemEntities()
        )
    }

    @Test
    fun featuredCategoriesWithDetails_toFeaturedCategoriesRequest_verifyFeaturedCategoriesRequest() {
        assertEquals(
            featuredCategoriesRequest,
            mapToFeaturedCategoriesRequest(featuredCategoriesWithDetails)
        )
    }
}
