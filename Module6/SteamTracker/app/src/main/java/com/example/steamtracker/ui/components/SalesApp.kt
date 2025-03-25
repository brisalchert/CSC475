package com.example.steamtracker.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.steamtracker.model.SteamSpyAppRequest
import com.example.steamtracker.utils.formatCurrency
import com.example.steamtracker.R
import com.example.steamtracker.model.AppDetails

@Composable
fun SalesApp(
    appInfo: SteamSpyAppRequest,
    appDetails: AppDetails?,
    navigateApp: () -> Unit,
    onAppSelect: (appId: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.padding(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        onClick = {
            onAppSelect(appInfo.appid)
            navigateApp()
        }
    ) {
        Column(
            modifier = modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.Start
        ) {
            AsyncImage(
                model = ImageRequest.Builder(context = LocalContext.current)
                    .data(appDetails?.headerImage)
                    .crossfade(true)
                    .build(),
                error = painterResource(R.drawable.ic_broken_image),
                placeholder = painterResource(R.drawable.loading_img),
                contentDescription = "Image for ${appDetails?.name}",
                contentScale = ContentScale.Crop,
                modifier = modifier.fillMaxWidth()
            )

            Column(
                modifier = Modifier.padding(8.dp)
            ) {
                Text(
                    text = appInfo.name ?: "Name Missing",
                    fontSize = 20.sp,
                    modifier = modifier.padding(start = 12.dp, top = 12.dp)
                )

                Row(
                    modifier = modifier.padding(start = 12.dp, bottom = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        // Prices will not be null, since these are specifically discounted games
                        text = formatCurrency(appInfo.initialprice!!.toInt().div(100.0)),
                        fontSize = 16.sp,
                        textDecoration = TextDecoration.LineThrough,
                        color = MaterialTheme.colorScheme.outline
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Text(
                        text = formatCurrency(appInfo.price!!.toInt().div(100.0)),
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Card(
                        modifier = modifier.wrapContentSize(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = colorResource(R.color.discount_background)
                        )
                    ) {
                        Text(
                            text = "-${appInfo.discount}%",
                            fontSize = 18.sp,
                            color = colorResource(R.color.discount_text),
                            modifier = modifier.padding(4.dp)
                        )
                    }
                }
            }
        }
    }
}
