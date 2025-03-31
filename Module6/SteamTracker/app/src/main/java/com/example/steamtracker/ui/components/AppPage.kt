package com.example.steamtracker.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.steamtracker.R
import com.example.steamtracker.model.AppDetails
import com.example.steamtracker.model.Screenshot
import com.example.steamtracker.model.SteamSpyAppRequest
import com.example.steamtracker.ui.screens.CollectionsViewModel
import com.example.steamtracker.utils.formatCurrency
import java.util.Locale

@Composable
fun AppPage(
    appDetails: AppDetails,
    appSpyInfo: SteamSpyAppRequest,
    newsAppsViewModel: NewsAppsViewModel,
    collectionsViewModel: CollectionsViewModel,
    navigateScreenshot: () -> Unit,
    onScreenshotSelect: (Screenshot) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth()
    ) {
        item {
            TitleCard(appDetails)
        }

        item {
            AsyncImage(
                model = ImageRequest.Builder(context = LocalContext.current)
                    .data(appDetails.headerImage)
                    .crossfade(true)
                    .build(),
                error = painterResource(R.drawable.ic_broken_image),
                placeholder = painterResource(R.drawable.loading_img),
                contentDescription = "Image for ${appDetails.name}",
                contentScale = ContentScale.Crop,
                modifier = modifier.fillMaxWidth()
            )
        }

        item {
            CollectionsRow(
                appDetails = appDetails,
                newsAppsViewModel = newsAppsViewModel,
                collectionsViewModel = collectionsViewModel
            )
        }

        item {
            GeneralInfo(appDetails, appSpyInfo, modifier)
        }

        item {
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primaryContainer)
            ) {
                ReviewScore(appSpyInfo, modifier)
            }
        }

        if (appDetails.screenshots != null) {
            item {
                Box(
                    modifier = modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.secondaryContainer)
                ) {
                    ScreenshotsRow(
                        appDetails = appDetails,
                        navigateScreenshot = navigateScreenshot,
                        onScreenshotSelect = onScreenshotSelect
                    )
                }
            }
        }

        item {
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.outlineVariant)
            ) {
                GenresRow(appDetails, modifier)
            }
        }

        if (appSpyInfo.tags != null) {
            item {
                Box(
                    modifier = modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surfaceDim)
                ) {
                    TagsRow(appSpyInfo, modifier)
                }
            }
        }

        item {
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.inversePrimary)
            ) {
                ShortInfo(appDetails, modifier)
            }
        }
    }
}

@Composable
fun TitleCard(
    appDetails: AppDetails
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
            text = appDetails.name.uppercase(),
            fontSize = 30.sp,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun CollectionsRow(
    appDetails: AppDetails,
    newsAppsViewModel: NewsAppsViewModel,
    collectionsViewModel: CollectionsViewModel,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()

    LazyRow(
        state = listState,
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth()
            .horizontalScrollbar(listState = listState),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(bottom = 8.dp)
    ) {
        item {
            WishlistBox(
                appDetails = appDetails,
                collectionsViewModel = collectionsViewModel
            )
        }

        item {
            FavoritesBox(
                appDetails = appDetails,
                collectionsViewModel = collectionsViewModel
            )
        }

        item {
            NewsListBox(
                appDetails = appDetails,
                newsAppsViewModel = newsAppsViewModel
            )
        }
    }
}

@Composable
fun GeneralInfo(
    appDetails: AppDetails,
    appSpyInfo: SteamSpyAppRequest,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(12.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = appDetails.name,
            fontSize = 24.sp
        )

        val priceInitial: Int
        val priceFinal: Int
        val discount: Int

        val hasAppDetailsPrice = appDetails.priceOverview != null
        val hasAppSpyInfoPrice = appSpyInfo.price != null && appSpyInfo.initialprice != null && appSpyInfo.discount != null

        // Check for price details or assign default values
        if (hasAppDetailsPrice) {
            priceInitial = appDetails.priceOverview.initial
            priceFinal = appDetails.priceOverview.final
            discount = appDetails.priceOverview.discountPercent
        } else if (hasAppSpyInfoPrice) {
            priceInitial = appSpyInfo.initialprice.toInt()
            priceFinal = appSpyInfo.price.toInt()
            discount = appSpyInfo.discount.toInt()
        } else {
            priceInitial = 0
            priceFinal = 0
            discount = 0
        }

        if (priceFinal == priceInitial && priceFinal == 0) {
            Box(
                modifier = modifier
                    .background(
                        colorResource(R.color.discount_background),
                        shape = RoundedCornerShape(8.dp)
                    )
            ) {
                Text(
                    text = "FREE",
                    color = colorResource(R.color.discount_text),
                    fontSize = 16.sp,
                    modifier = modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                )
            }
        } else if (discount > 0) {
            Row(
                modifier = modifier,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = formatCurrency(priceInitial.div(100.0)),
                    fontSize = 16.sp,
                    textDecoration = TextDecoration.LineThrough,
                    color = MaterialTheme.colorScheme.outline
                )

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = formatCurrency(priceFinal.div(100.0)),
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
                        text = "-${discount}%",
                        fontSize = 18.sp,
                        color = colorResource(R.color.discount_text),
                        modifier = modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
            }
        } else {
            if (priceFinal == 0) {
                Box(
                    modifier = modifier
                        .background(
                            colorResource(R.color.discount_background),
                            shape = RoundedCornerShape(8.dp)
                        )
                ) {
                    Text(
                        text = "FREE",
                        color = colorResource(R.color.discount_text),
                        fontSize = 16.sp,
                        modifier = modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
            } else {
                Text(
                    text = formatCurrency(priceFinal.div(100.0)),
                    fontSize = 16.sp
                )
            }
        }

        Text(
            text = "Developers: ${appDetails.developers?.joinToString(", ").toString()}"
        )

        Text(
            text = "Publishers: ${appDetails.publishers?.joinToString(", ")}"
        )

        Text(
            text = "Release Date: ${appDetails.releaseDate.date}"
        )
    }
}

@Composable
fun ReviewScore(
    appSpyInfo: SteamSpyAppRequest,
    modifier: Modifier = Modifier
) {
    if (appSpyInfo.positive > 0 && appSpyInfo.negative > 0) {
        val percentPositive = (appSpyInfo.positive.toDouble()
            .div(appSpyInfo.positive.plus(appSpyInfo.negative))).times(100)

        var cardColor = colorResource(R.color.reviews_positive)

        if (percentPositive < 60) {
            cardColor = colorResource(R.color.reviews_negative)
        } else if (percentPositive < 80) {
            cardColor = colorResource(R.color.reviews_neutral)
        }

        Card(
            modifier = modifier.padding(12.dp),
            elevation = CardDefaults.cardElevation(4.dp),
            colors = CardDefaults.cardColors(
                containerColor = cardColor
            )
        ) {
            Text(
                text = "Reviews: ${String.format(Locale.US, "%.1f", percentPositive)}% positive",
                modifier = modifier.padding(8.dp),
                color = colorResource(R.color.black)
            )
        }
    } else {
        Card(
            modifier = modifier.padding(12.dp),
            elevation = CardDefaults.cardElevation(4.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.outlineVariant
            )
        ) {
            Text(
                text = "Reviews Unavailable",
                modifier = modifier.padding(8.dp)
            )
        }
    }
}

@Composable
fun ScreenshotsRow(
    appDetails: AppDetails,
    navigateScreenshot: () -> Unit,
    onScreenshotSelect: (screenshot: Screenshot) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "SCREENSHOTS",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            modifier = modifier.padding(horizontal = 12.dp)
        )

        LazyRow(
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 4.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(items = appDetails.screenshots!!) { screenshot ->
                Card(
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 8.dp),
                    elevation = CardDefaults.cardElevation(8.dp),
                    onClick = {
                        onScreenshotSelect(screenshot)
                        navigateScreenshot()
                    }
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(context = LocalContext.current)
                            .data(screenshot.pathThumbnail)
                            .crossfade(true)
                            .build(),
                        error = painterResource(R.drawable.ic_broken_image),
                        placeholder = painterResource(R.drawable.loading_img),
                        contentDescription = "Image for ${appDetails.name}",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun GenresRow(
    appDetails: AppDetails,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.outlineVariant),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "GENRES:",
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            modifier = modifier.padding(horizontal = 12.dp)
        )

        LazyRow(
            state = listState,
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 4.dp)
                .fillMaxWidth()
                .horizontalScrollbar(
                    listState = listState,
                    color = MaterialTheme.colorScheme.outline
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                appDetails.genres?.forEach { genre ->
                    Genre(
                        genre.description,
                        true,
                        modifier = Modifier.padding(
                            start = 4.dp,
                            end = 4.dp,
                            top = 8.dp,
                            bottom = 16.dp
                        )
                    ) // TODO: Add genre favorites
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class, ExperimentalFoundationApi::class)
@Composable
fun TagsRow(
    appSpyInfo: SteamSpyAppRequest,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceDim),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "TAGS:",
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            modifier = modifier.padding(horizontal = 12.dp)
        )

        LazyRow(
            state = listState,
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 4.dp)
                .fillMaxWidth()
                .horizontalScrollbar(
                    listState = listState,
                    color = MaterialTheme.colorScheme.outline
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Use only one item to load all tags at once
            item {
                appSpyInfo.tags?.keys?.forEach { key ->
                    Tag(
                        tag = key,
                        favorite = true,
                        modifier = Modifier.padding(
                            start = 4.dp,
                            end = 4.dp,
                            top = 8.dp,
                            bottom = 16.dp
                        )
                    ) // TODO: Add tag favorites
                }
            }
        }
    }
}

@Composable
fun ShortInfo(
    appDetails: AppDetails,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(12.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = appDetails.shortDescription,
            fontStyle = FontStyle.Italic,
            fontSize = 20.sp,
            modifier = modifier.padding(vertical = 20.dp)
        )
    }
}
