package com.example.steamtracker.utils

import com.example.steamtracker.model.NewsItem
import com.example.steamtracker.room.entities.NewsItemEntity
import com.example.steamtracker.room.entities.NewsItemNotificationEntity

fun NewsItem.toNewsItemEntity(): NewsItemEntity {
    return NewsItemEntity(
        gid = this.gid,
        title = this.title,
        url = this.url,
        isExternalUrl = this.isExternalUrl,
        author = this.author,
        contents = this.contents,
        feedlabel = this.feedlabel,
        date = this.date,
        feedname = this.feedname,
        feedType = this.feedType,
        appid = this.appid
    )
}

fun NewsItemEntity.toNewsItem(): NewsItem {
    return NewsItem(
        gid = this.gid,
        title = this.title,
        url = this.url,
        isExternalUrl = this.isExternalUrl,
        author = this.author,
        contents = this.contents,
        feedlabel = this.feedlabel,
        date = this.date,
        feedname = this.feedname,
        feedType = this.feedType,
        appid = this.appid
    )
}

fun NewsItem.toNewsItemNotificationEntity(timestamp: Long): NewsItemNotificationEntity {
    return NewsItemNotificationEntity(
        gid = this.gid,
        title = this.title,
        url = this.url,
        isExternalUrl = this.isExternalUrl,
        author = this.author,
        contents = this.contents,
        feedlabel = this.feedlabel,
        date = this.date,
        feedname = this.feedname,
        feedType = this.feedType,
        appid = this.appid,
        timestamp = timestamp
    )
}

fun NewsItemNotificationEntity.toNewsItem(): NewsItem {
    return NewsItem(
        gid = this.gid,
        title = this.title,
        url = this.url,
        isExternalUrl = this.isExternalUrl,
        author = this.author,
        contents = this.contents,
        feedlabel = this.feedlabel,
        date = this.date,
        feedname = this.feedname,
        feedType = this.feedType,
        appid = this.appid
    )
}
