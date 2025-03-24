package com.example.steamtracker.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.steamtracker.R
import com.example.steamtracker.mock.MockSteamworksRepository
import com.example.steamtracker.model.AppDetails
import com.example.steamtracker.room.relations.AppNewsWithDetails
import com.example.steamtracker.ui.theme.SteamTrackerTheme
import kotlinx.coroutines.flow.asFlow

@Composable
fun NewsListBox(
    appDetails: AppDetails,
    onList: Boolean,
    newsAppsViewModel: NewsAppsViewModel,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.padding(4.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(R.color.collections_container)
        ),
        onClick = {  }
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val image: ImageVector = if (onList) {
                Icons.Default.Check
            } else {
                Icons.Default.Add
            }

            Icon(
                imageVector = image,
                contentDescription = "News List Status",
                tint = colorResource(R.color.collections_text)
            )

            Text(
                text = "Track News",
                fontSize = 14.sp,
                color = colorResource(R.color.collections_text)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NewsListBoxPreview() {
    SteamTrackerTheme {
        NewsListBox(
            appDetails = AppDetails(),
            onList = false,
            newsAppsViewModel = NewsAppsViewModel(
                MockSteamworksRepository(
                    listOf(listOf<AppNewsWithDetails>()).asFlow(),
                    listOf(listOf<Int>()).asFlow()
                )
            )
        )
    }
}
