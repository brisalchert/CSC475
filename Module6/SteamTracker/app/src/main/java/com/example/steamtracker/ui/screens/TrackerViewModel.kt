package com.example.steamtracker.ui.screens

import androidx.lifecycle.ViewModel

sealed interface TrackerUiState {
    data object Success: TrackerUiState
    data object Error : TrackerUiState
    data object Loading : TrackerUiState
}

class TrackerViewModel(

): ViewModel() {

}
