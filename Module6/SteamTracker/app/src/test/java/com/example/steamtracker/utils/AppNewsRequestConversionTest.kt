package com.example.steamtracker.utils

import com.example.steamtracker.model.AppNews
import com.example.steamtracker.model.AppNewsRequest
import com.example.steamtracker.model.NewsItem
import com.example.steamtracker.room.entities.AppNewsEntity
import com.example.steamtracker.room.entities.AppNewsRequestEntity
import com.example.steamtracker.room.entities.NewsItemEntity
import com.example.steamtracker.room.relations.AppNewsWithDetails
import com.example.steamtracker.room.relations.AppNewsWithItems
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test

class AppNewsRequestConversionTest {
    private lateinit var appNewsRequest: AppNewsRequest
    private lateinit var appNewsRequestEntity: AppNewsRequestEntity
    private lateinit var appNewsEntity: AppNewsEntity
    private lateinit var newsItemEntities: List<NewsItemEntity>
    private lateinit var appNewsWithDetails: AppNewsWithDetails

    @Before
    fun setup() {
        appNewsRequest = AppNewsRequest(
            appnews = AppNews(
                appid = 1,
                newsitems = listOf(
                    NewsItem(
                        gid = "1",
                        title = "title1",
                        url = "url1",
                        isExternalUrl = true,
                        author = "author1",
                        contents = "contents1",
                        feedlabel = "label1",
                        date = 12L,
                        feedname = "feedname1",
                        feedType = 1,
                        appid = 1
                    ),
                    NewsItem(
                        gid = "2",
                        title = "title2",
                        url = "url2",
                        isExternalUrl = false,
                        author = "author2",
                        contents = "contents2",
                        feedlabel = "label2",
                        date = 13L,
                        feedname = "feedname2",
                        feedType = 2,
                        appid = 2
                    )
                )
            )
        )

        appNewsRequestEntity = AppNewsRequestEntity(
            appid = 1,
            lastUpdated = 0L
        )

        appNewsEntity = AppNewsEntity(
            appid = 1
        )

        newsItemEntities = listOf(
            NewsItemEntity(
                gid = "1",
                title = "title1",
                url = "url1",
                isExternalUrl = true,
                author = "author1",
                contents = "contents1",
                feedlabel = "label1",
                date = 12L,
                feedname = "feedname1",
                feedType = 1,
                appid = 1
            ),
            NewsItemEntity(
                gid = "2",
                title = "title2",
                url = "url2",
                isExternalUrl = false,
                author = "author2",
                contents = "contents2",
                feedlabel = "label2",
                date = 13L,
                feedname = "feedname2",
                feedType = 2,
                appid = 2
            )
        )

        appNewsWithDetails = AppNewsWithDetails(
            request = appNewsRequestEntity,
            appNewsWithItems = AppNewsWithItems(
                appnews = appNewsEntity,
                newsitems = newsItemEntities
            )
        )
    }

    @Test
    fun appNewsRequest_toAppNewsRequestEntity_verifyAppNewsRequestEntity() {
        assertEquals(
            appNewsRequestEntity,
            appNewsRequest.toAppNewsRequestEntity().copy(lastUpdated = 0L)
        )
    }

    @Test
    fun appNewsRequest_toAppNewsEntity_verifyAppNewsEntity() {
        assertEquals(
            appNewsEntity,
            appNewsRequest.toAppNewsEntity()
        )
    }

    @Test
    fun appNewsRequest_toNewsItemEntities_verifyNewsItemEntities() {
        assertEquals(
            newsItemEntities,
            appNewsRequest.toNewsItemEntities()
        )
    }

    @Test
    fun appNewsWithDetails_toAppNewsRequest_verifyAppNewsRequest() {
        assertEquals(
            appNewsRequest,
            appNewsWithDetails.toAppNewsRequest()
        )
    }
}
