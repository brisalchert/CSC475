package com.example.steamtracker.ui.components

import android.widget.Toast
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.steamtracker.R
import com.example.steamtracker.model.AppDetails
import com.example.steamtracker.ui.preview.FakeCollectionsRepository
import com.example.steamtracker.ui.preview.FakeStoreRepository
import com.example.steamtracker.ui.screens.CollectionsViewModel
import com.example.steamtracker.ui.theme.SteamTrackerTheme

@Composable
fun FavoritesBox(
    appDetails: AppDetails,
    collectionsViewModel: CollectionsViewModel,
    modifier: Modifier = Modifier
) {
    val onList by collectionsViewModel.isInCollection("Favorites", appDetails.steamAppId).collectAsState(
        initial = false
    )
    val context = LocalContext.current

    Card(
        modifier = modifier.padding(4.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(R.color.collections_container)
        ),
        onClick = {
            if (onList) {
                collectionsViewModel.removeCollectionApp("Favorites", appDetails.steamAppId)
                Toast.makeText(
                    context,
                    "Removed ${appDetails.name} from Favorites",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                collectionsViewModel.addCollectionApp("Favorites", appDetails.steamAppId)
                Toast.makeText(
                    context,
                    "Added ${appDetails.name} to Favorites",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
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

            val contentDescription = if (onList) {
                "Added to Favorites"
            } else {
                "Not on Favorites"
            }

            Icon(
                imageVector = image,
                contentDescription = contentDescription,
                tint = colorResource(R.color.collections_text)
            )

            Text(
                text = "Add to Favorites",
                fontSize = 16.sp,
                color = colorResource(R.color.collections_text)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FavoritesBoxPreview() {
    SteamTrackerTheme {
        FavoritesBox(
            appDetails = AppDetails(),
            collectionsViewModel = CollectionsViewModel(
                storeRepository = FakeStoreRepository(),
                collectionsRepository = FakeCollectionsRepository(),
                workManager = null
            )
        )
    }
}
