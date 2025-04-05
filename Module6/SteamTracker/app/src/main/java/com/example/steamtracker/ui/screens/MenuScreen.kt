package com.example.steamtracker.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CollectionsBookmark
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.steamtracker.ui.TrackerMainScreens
import com.example.steamtracker.ui.TrackerOtherScreens
import com.example.steamtracker.ui.theme.SteamTrackerTheme

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MenuScreen(
    navigation: Map<String, () -> Unit>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .shadow(8.dp, RoundedCornerShape(0.dp))
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "MENU",
                fontSize = 30.sp,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                textAlign = TextAlign.Center
            )
        }

        Card(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            val icons = mapOf(
                TrackerMainScreens.Store.name to Icons.Filled.Store,
                TrackerMainScreens.News.name to Icons.Filled.Newspaper,
                TrackerMainScreens.Collections.name to Icons.Filled.CollectionsBookmark,
                TrackerMainScreens.Notifications.name to Icons.Filled.Notifications,
                TrackerOtherScreens.Search.name to Icons.Filled.Search,
                TrackerOtherScreens.Preferences.name to Icons.Filled.Favorite,
                TrackerOtherScreens.Settings.name to Icons.Filled.Settings
            )

            LazyColumn(
                modifier = Modifier
                    .padding(12.dp)
                    .testTag("MenuList"),
                verticalArrangement = Arrangement.Top
            ) {
                itemsIndexed(icons.entries.toList()) { index, (label, icon) ->
                    MenuItem(
                        label = label,
                        icon = icon,
                        navigate = navigation[label] ?: {}
                    )

                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 8.dp),
                        thickness = 1.dp,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MenuScreenPreview() {
    SteamTrackerTheme {
        MenuScreen(
            navigation = mapOf(
                "Screen" to {},
                "Screen" to {},
                "Screen" to {}
            )
        )
    }
}

@Composable
fun MenuItem(
    label: String,
    icon: ImageVector,
    navigate: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(0.dp),
        onClick = navigate
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(8.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label
            )

            Text(
                text = label,
                fontSize = 20.sp,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}
