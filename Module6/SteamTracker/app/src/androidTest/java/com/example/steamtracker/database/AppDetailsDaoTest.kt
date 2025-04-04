package com.example.steamtracker.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import com.example.steamtracker.TestDatabase
import com.example.steamtracker.fake.FakeAppDetailsRequest
import com.example.steamtracker.room.dao.AppDetailsDao
import com.example.steamtracker.utils.toAppDetailsEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.equalTo
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.IOException
import kotlin.jvm.Throws

class AppDetailsDaoTest {
    private lateinit var appDetailsDao: AppDetailsDao
    private lateinit var db: TestDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context,
            TestDatabase::class.java
        ).build()
        appDetailsDao = db.appDetailsDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun writeAppDetailsAndReadInList() =
        runTest {
            val fakeAppDetailsEntity = FakeAppDetailsRequest.response["0"]!!.appDetails?.toAppDetailsEntity()!!

            appDetailsDao.insertAppDetails(fakeAppDetailsEntity)

            val allAppDetails = appDetailsDao.getAllAppDetails().first()

            assertThat(allAppDetails[0], equalTo(fakeAppDetailsEntity))
        }

    @Test
    @Throws(Exception::class)
    fun writeAppDetailsAndReadById() =
        runTest {
            val fakeAppDetailsEntity = FakeAppDetailsRequest.response["0"]!!.appDetails?.toAppDetailsEntity()!!
            val appId = FakeAppDetailsRequest.response["0"]!!.appDetails?.steamAppId!!

            appDetailsDao.insertAppDetails(fakeAppDetailsEntity)

            val appDetailsById = appDetailsDao.getAppDetails(appId)

            assertThat(appDetailsById, equalTo(fakeAppDetailsEntity))
        }
}
