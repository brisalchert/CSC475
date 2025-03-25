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
import com.example.steamtracker.model.AppDetails
import com.example.steamtracker.ui.theme.SteamTrackerTheme

@Composable
fun WishlistBox(
    appDetails: AppDetails,
    onList: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.padding(4.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(R.color.collections_container)
        ),
        onClick = { onClick() }
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
                contentDescription = "Wishlist Status",
                tint = colorResource(R.color.collections_text)
            )

            Text(
                text = "Add to Wishlist",
                fontSize = 18.sp,
                color = colorResource(R.color.collections_text)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WishlistBoxPreview() {
    SteamTrackerTheme {
        WishlistBox(
            appDetails = AppDetails(),
            onList = false,
            onClick = {}
        )
    }
}
