package com.example.steamtracker.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.steamtracker.model.AppDetails

@Composable
fun AppPage(
    appDetails: AppDetails?,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    if (appDetails == null) {
        Text("App Not Found")
    } else {
        AppText(appDetails, modifier, contentPadding)
    }
}

@Composable
fun AppText(
    appDetails: AppDetails,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    Column {
        Text("Game Name: " + appDetails.name)
        Text("Game ID: " + appDetails.steamAppId.toString())
        Text("Short Description:\n" + appDetails.shortDescription)
    }
}
