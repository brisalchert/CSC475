package com.example.steamtracker.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import com.example.steamtracker.TestDatabase
import com.example.steamtracker.fake.FakeAppNewsRequest
import com.example.steamtracker.room.dao.SteamworksDao
import com.example.steamtracker.room.entities.AppNewsEntity
import com.example.steamtracker.room.entities.AppNewsRequestEntity
import com.example.steamtracker.room.relations.AppNewsWithDetails
import com.example.steamtracker.room.relations.AppNewsWithItems
import com.example.steamtracker.utils.toNewsItemEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.equalTo
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.IOException

class SteamworksDaoTest {
    private lateinit var steamworksDao: SteamworksDao
    private lateinit var db: TestDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context,
            TestDatabase::class.java
        ).build()
        steamworksDao = db.steamworksDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun writeNewsRequestAndReadInList() =
        runTest {
            val fakeAppNewsRequestEntity = AppNewsRequestEntity(
                appid = FakeAppNewsRequest.response.appnews.appid,
                lastUpdated = 0L
            )
            val fakeAppNewsEntity = AppNewsEntity(
                appid = FakeAppNewsRequest.response.appnews.appid
            )
            val fakeNewsItemEntities = FakeAppNewsRequest.response.appnews.newsitems.map {
                it.toNewsItemEntity()
            }
            val fakeAppNewsWithDetails = AppNewsWithDetails(
                request = fakeAppNewsRequestEntity,
                appNewsWithItems = AppNewsWithItems(
                    appnews = fakeAppNewsEntity,
                    newsitems = fakeNewsItemEntities
                )
            )

            steamworksDao.insertAppNewsRequests(listOf(fakeAppNewsRequestEntity))
            steamworksDao.insertAppNews(listOf(fakeAppNewsEntity))
            steamworksDao.insertNews(fakeNewsItemEntities)

            var allAppNews = steamworksDao.getAllAppNews().first()

            // Test AppDetails fetched
            assertThat(allAppNews[0], equalTo(fakeAppNewsWithDetails))
        }

    @Test
    @Throws(Exception::class)
    fun writeNewsAndReadByGid() =
        runTest {
            val fakeAppNewsRequestEntity = AppNewsRequestEntity(
                appid = FakeAppNewsRequest.response.appnews.appid,
                lastUpdated = 0L
            )
            val fakeAppNewsEntity = AppNewsEntity(
                appid = FakeAppNewsRequest.response.appnews.appid
            )
            val fakeNewsItemEntities = FakeAppNewsRequest.response.appnews.newsitems.map {
                it.toNewsItemEntity()
            }
            val fakeAppNewsWithDetails = AppNewsWithDetails(
                request = fakeAppNewsRequestEntity,
                appNewsWithItems = AppNewsWithItems(
                    appnews = fakeAppNewsEntity,
                    newsitems = fakeNewsItemEntities
                )
            )
            val newsItemGid = fakeNewsItemEntities.first().gid

            steamworksDao.insertAppNewsRequests(listOf(fakeAppNewsRequestEntity))
            steamworksDao.insertAppNews(listOf(fakeAppNewsEntity))
            steamworksDao.insertNews(fakeNewsItemEntities)

            var appNews = steamworksDao.getNewsByGid(newsItemGid)

            // Test AppDetails fetched
            assertThat(appNews, equalTo(fakeNewsItemEntities.first()))
        }
}
