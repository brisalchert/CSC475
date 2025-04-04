package com.example.steamtracker.fake

import com.example.steamtracker.model.AppNews
import com.example.steamtracker.model.AppNewsRequest
import com.example.steamtracker.model.NewsItem

object FakeAppNewsRequest {
    val response: AppNewsRequest = AppNewsRequest(
        appnews = AppNews(
            newsitems = listOf(NewsItem(gid = "0"))
        )
    )
}
