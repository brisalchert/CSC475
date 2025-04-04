package com.example.steamtracker.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import com.example.steamtracker.TestDatabase
import com.example.steamtracker.fake.FakeSteamSpyAppRequest
import com.example.steamtracker.room.dao.SpyDao
import com.example.steamtracker.room.entities.TagEntity
import com.example.steamtracker.room.relations.SteamSpyAppWithTags
import com.example.steamtracker.utils.toSteamSpyAppEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.equalTo
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.IOException

class SpyDaoTest {
    private lateinit var spyDao: SpyDao
    private lateinit var db: TestDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context,
            TestDatabase::class.java
        ).build()
        spyDao = db.salesDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun writeSpyInfoAndReadInList() =
        runTest {
            val appId = FakeSteamSpyAppRequest.response["gameId"]!!.appid
            val fakeSteamSpyAppEntity = FakeSteamSpyAppRequest.response["gameId"]!!.toSteamSpyAppEntity()
            val fakeSteamSpyAppTagEntities = FakeSteamSpyAppRequest.response["gameId"]!!.tags?.map {
                TagEntity(
                    appid = appId,
                    tagName = it.key,
                    tagCount = it.value
                )
            } ?: emptyList()
            val fakeSteamSpyAppWithTags = SteamSpyAppWithTags(
                app = fakeSteamSpyAppEntity,
                tags = fakeSteamSpyAppTagEntities
            )

            spyDao.insertAppInfoWithTags(
                spyEntities = listOf(fakeSteamSpyAppEntity),
                tagEntities = listOf(fakeSteamSpyAppTagEntities)
            )

            var allGames = spyDao.getAllGames().first()

            // Test game fetched
            assertThat(allGames[0], equalTo(fakeSteamSpyAppWithTags))
        }

    @Test
    @Throws(Exception::class)
    fun writeSpyInfoAndReadById() =
        runTest {
            val appId = FakeSteamSpyAppRequest.response["gameId"]!!.appid
            val fakeSteamSpyAppEntity = FakeSteamSpyAppRequest.response["gameId"]!!.toSteamSpyAppEntity()
            val fakeSteamSpyAppTagEntities = FakeSteamSpyAppRequest.response["gameId"]!!.tags?.map {
                TagEntity(
                    appid = appId,
                    tagName = it.key,
                    tagCount = it.value
                )
            } ?: emptyList()
            val fakeSteamSpyAppWithTags = SteamSpyAppWithTags(
                app = fakeSteamSpyAppEntity,
                tags = fakeSteamSpyAppTagEntities
            )

            spyDao.insertAppInfoWithTags(
                spyEntities = listOf(fakeSteamSpyAppEntity),
                tagEntities = listOf(fakeSteamSpyAppTagEntities)
            )

            var game = spyDao.getSpyInfo(appId)

            // Test game fetched
            assertThat(game, equalTo(fakeSteamSpyAppWithTags))
        }
}
