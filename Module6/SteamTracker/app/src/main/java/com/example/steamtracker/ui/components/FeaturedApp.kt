package com.example.steamtracker.ui.components

import androidx.compose.foundation.layout.Arrangement
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
import com.example.steamtracker.model.AppInfo
import com.example.steamtracker.R
import com.example.steamtracker.utils.formatCurrency

@Composable
fun FeaturedApp(
    appInfo: AppInfo,
    navigateApp: () -> Unit,
    onAppSelect: (appId: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.padding(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        onClick = {
            onAppSelect(appInfo.id)
            navigateApp()
        }
    ) {
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.Start
        ) {
            AsyncImage(
                model = ImageRequest.Builder(context = LocalContext.current)
                    .data(appInfo.largeCapsuleImage)
                    .crossfade(true)
                    .build(),
                error = painterResource(R.drawable.ic_broken_image),
                placeholder = painterResource(R.drawable.loading_img),
                contentDescription = "Image for ${appInfo.name}",
                contentScale = ContentScale.Crop,
                modifier = modifier.fillMaxWidth()
            )

            Text(
                text = appInfo.name,
                fontSize = 20.sp,
                modifier = modifier.padding(horizontal = 8.dp)
            )

            if (appInfo.finalPrice != appInfo.originalPrice) {
                Row(
                    modifier = modifier.padding(start = 8.dp, bottom = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = formatCurrency(appInfo.originalPrice.div(100.0)),
                        fontSize = 16.sp,
                        textDecoration = TextDecoration.LineThrough,
                        color = MaterialTheme.colorScheme.outline
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Text(
                        text = formatCurrency(appInfo.finalPrice.div(100.0)),
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
                            text = "-${appInfo.discountPercent}%",
                            fontSize = 18.sp,
                            color = colorResource(R.color.discount_text),
                            modifier = modifier.padding(4.dp)
                        )
                    }
                }
            } else {
                Text(
                    text = formatCurrency(appInfo.finalPrice.div(100.0)),
                    fontSize = 16.sp,
                    modifier = modifier.padding(start = 8.dp, bottom = 12.dp)
                )
            }
        }
    }
}
