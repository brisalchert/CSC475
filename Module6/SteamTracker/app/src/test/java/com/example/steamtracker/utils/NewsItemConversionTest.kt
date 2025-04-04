package com.example.steamtracker.utils

import com.example.steamtracker.model.NewsItem
import com.example.steamtracker.room.entities.NewsItemEntity
import com.example.steamtracker.room.entities.NewsItemNotificationEntity
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test

class NewsItemConversionTest {
    private lateinit var newsItem: NewsItem
    private lateinit var newsItemEntity: NewsItemEntity
    private lateinit var newsItemNotificationEntity: NewsItemNotificationEntity

    @Before
    fun setup() {
        newsItem = NewsItem(
            gid = "12",
            title = "title",
            url = "url",
            isExternalUrl = false,
            author = "author",
            contents = "contents",
            feedlabel = "feedlabel",
            date = 1230L,
            feedname = "feedname",
            feedType = 23,
            appid = 7
        )

        newsItemEntity = NewsItemEntity(
            gid = "12",
            title = "title",
            url = "url",
            isExternalUrl = false,
            author = "author",
            contents = "contents",
            feedlabel = "feedlabel",
            date = 1230L,
            feedname = "feedname",
            feedType = 23,
            appid = 7
        )

        newsItemNotificationEntity = NewsItemNotificationEntity(
            gid = "12",
            title = "title",
            url = "url",
            isExternalUrl = false,
            author = "author",
            contents = "contents",
            feedlabel = "feedlabel",
            date = 1230L,
            feedname = "feedname",
            feedType = 23,
            appid = 7,
            timestamp = 0L
        )
    }

    @Test
    fun newsItem_toNewsItemEntity_verifyNewsItemEntity() {
        assertEquals(
            newsItemEntity,
            newsItem.toNewsItemEntity()
        )
    }

    @Test
    fun newsItemEntity_toNewsItem_verifyNewsItem() {
        assertEquals(
            newsItem,
            newsItemEntity.toNewsItem()
        )
    }

    @Test
    fun newsItem_toNewsItemNotificationEntity_verifyNewsItemNotificationEntity() {
        assertEquals(
            newsItemNotificationEntity,
            newsItem.toNewsItemNotificationEntity(0L)
        )
    }

    @Test
    fun newsItemNotificationEntity_toNewsItem_verifyNewsItem() {
        assertEquals(
            newsItem,
            newsItemNotificationEntity.toNewsItem()
        )
    }
}
