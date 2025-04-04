package com.example.steamtracker.fake

import com.example.steamtracker.model.FeaturedCategoriesRequest
import com.example.steamtracker.model.RegularCategory
import com.example.steamtracker.model.StaticCategory

object FakeFeaturedCategoriesRequest {
    val response: FeaturedCategoriesRequest = FeaturedCategoriesRequest(
        specials = RegularCategory(name = "Specials"),
        comingSoon = RegularCategory(name = "Coming Soon"),
        topSellers = RegularCategory(name = "Top Sellers"),
        newReleases = RegularCategory(name = "New Releases"),
        genres = StaticCategory(name = "Genres"),
        trailerslideshow = StaticCategory(name = "Trailer Slideshow"),
        status = 1
    )
}
