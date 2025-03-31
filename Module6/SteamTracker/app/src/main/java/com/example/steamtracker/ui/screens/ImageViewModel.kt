package com.example.steamtracker.ui.screens

import androidx.lifecycle.ViewModel
import com.example.steamtracker.model.Screenshot
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ImageViewModel(): ViewModel() {
    private val _screenshot = MutableStateFlow<Screenshot>(Screenshot())
    val screenshot: StateFlow<Screenshot> = _screenshot.asStateFlow()

    fun setScreenshot(screenshot: Screenshot) {
        _screenshot.value = screenshot
    }
}
