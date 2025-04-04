package com.example.steamtracker.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import com.example.steamtracker.TestDatabase
import com.example.steamtracker.fake.FakeAppNewsRequest
import com.example.steamtracker.room.dao.NewsAppsDao
import com.example.steamtracker.room.entities.NewsAppEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.equalTo
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.IOException

class NewsAppsDaoTest {
    private lateinit var newsAppsDao: NewsAppsDao
    private lateinit var db: TestDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context,
            TestDatabase::class.java
        ).build()
        newsAppsDao = db.newsAppsDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun writeNewsAppAndReadInList() =
        runTest {
            val fakeNewsAppEntity = NewsAppEntity(
                appid = FakeAppNewsRequest.response.appnews.appid
            )

            newsAppsDao.insertNewsApp(fakeNewsAppEntity)

            var allNewsAppIds = newsAppsDao.getNewsAppIds().first()

            // Test NewsAppId fetched
            assertThat(allNewsAppIds[0], equalTo(fakeNewsAppEntity.appid))

            newsAppsDao.deleteNewsApp(FakeAppNewsRequest.response.appnews.appid)

            allNewsAppIds = newsAppsDao.getNewsAppIds().first()

            // Test NewsAppId removed
            assertThat(allNewsAppIds, equalTo(emptyList()))
        }

    @Test
    @Throws(Exception::class)
    fun writeNewsAppAndReadById() =
        runTest {
            val fakeNewsAppEntity = NewsAppEntity(
                appid = FakeAppNewsRequest.response.appnews.appid
            )

            newsAppsDao.insertNewsApp(fakeNewsAppEntity)

            var newsAppId = newsAppsDao.checkForId(fakeNewsAppEntity.appid)

            // Test NewsAppId fetched
            assertThat(newsAppId, equalTo(fakeNewsAppEntity.appid))

            newsAppsDao.deleteNewsApp(FakeAppNewsRequest.response.appnews.appid)

            newsAppId = newsAppsDao.checkForId(fakeNewsAppEntity.appid)

            // Test NewsAppId removed
            assertThat(newsAppId, equalTo(null))
        }
}
