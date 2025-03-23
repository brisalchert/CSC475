package com.example.steamtracker.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.steamtracker.model.SteamSpyAppRequest
import com.example.steamtracker.utils.formatCurrency
import com.example.steamtracker.R

@Composable
fun SalesApp(
    appInfo: SteamSpyAppRequest,
    modifier: Modifier = Modifier
) {
    Card( // TODO: Implement clicking on app to visit details page
        modifier = modifier.padding(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = appInfo.name,
                fontSize = 20.sp,
                modifier = modifier.padding(start = 12.dp, top = 12.dp)
            )

            Row(
                modifier = modifier.padding(start = 12.dp, bottom = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = formatCurrency(appInfo.initialprice.toInt().div(100.0)),
                    fontSize = 16.sp,
                    textDecoration = TextDecoration.LineThrough,
                    color = MaterialTheme.colorScheme.outline
                )

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = formatCurrency(appInfo.price.toInt().div(100.0)),
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
