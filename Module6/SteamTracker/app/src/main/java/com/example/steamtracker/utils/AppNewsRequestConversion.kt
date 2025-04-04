package com.example.steamtracker.utils

import com.example.steamtracker.model.AppNews
import com.example.steamtracker.model.AppNewsRequest
import com.example.steamtracker.room.entities.AppNewsEntity
import com.example.steamtracker.room.entities.AppNewsRequestEntity
import com.example.steamtracker.room.entities.NewsItemEntity
import com.example.steamtracker.room.relations.AppNewsWithDetails

fun AppNewsRequest.toAppNewsRequestEntity(): AppNewsRequestEntity {
    return AppNewsRequestEntity(
        appid = this.appnews.appid,
        lastUpdated = System.currentTimeMillis()
    )
}

fun AppNewsRequest.toAppNewsEntity(): AppNewsEntity {
    return AppNewsEntity(
        appid = this.appnews.appid
    )
}

fun AppNewsRequest.toNewsItemEntities(): List<NewsItemEntity> {
    return this.appnews.newsitems.map { it.toNewsItemEntity() }
}

fun AppNewsWithDetails.toAppNewsRequest(): AppNewsRequest {
        val newsItems = this.appNewsWithItems.newsitems.map { it.toNewsItem() }

        val appNews = AppNews(
            appid = newsItems.first().appid,
            newsitems = newsItems
        )

    return AppNewsRequest(
        appnews = appNews
    )
}
