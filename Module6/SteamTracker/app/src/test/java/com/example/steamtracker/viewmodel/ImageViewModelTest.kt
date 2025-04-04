package com.example.steamtracker.viewmodel

import com.example.steamtracker.fake.FakeAppDetailsRequest
import com.example.steamtracker.rules.TestDispatcherRule
import com.example.steamtracker.ui.screens.ImageViewModel
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ImageViewModelTest {
    @get:Rule
    val testDispatcher = TestDispatcherRule()

    private lateinit var imageViewModel: ImageViewModel

    @Before
    fun setup() {
        imageViewModel = ImageViewModel()
    }

    @Test
    fun imageViewModel_setScreenshot_verifyScreenshotCorrect() =
        runTest {
            val fakeScreenshot = FakeAppDetailsRequest.response["0"]!!.appDetails?.screenshots?.first()

            assertEquals(
                fakeScreenshot,
                imageViewModel.screenshot.first()
            )
        }
}
