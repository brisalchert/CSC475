package com.example.steamtracker.fake

import com.example.steamtracker.model.FeaturedCategoriesRequest
import com.example.steamtracker.model.RegularCategory
import com.example.steamtracker.model.StaticCategory

object FakeFeaturedCategoriesRequest {
    val response: FeaturedCategoriesRequest = FeaturedCategoriesRequest(
        specials = RegularCategory(id = "0", name = "Specials"),
        comingSoon = RegularCategory(id = "1", name = "Coming Soon"),
        topSellers = RegularCategory(id = "2", name = "Top Sellers"),
        newReleases = RegularCategory(id = "3", name = "New Releases"),
        genres = StaticCategory(id = "4", name = "Genres"),
        trailerslideshow = StaticCategory(id = "5", name = "Trailer Slideshow"),
        status = 1
    )
}
