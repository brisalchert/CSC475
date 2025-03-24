package com.example.steamtracker.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.steamtracker.R
import com.example.steamtracker.mock.MockSteamworksRepository
import com.example.steamtracker.model.Achievement
import com.example.steamtracker.model.AchievementsContainer
import com.example.steamtracker.model.AppDetails
import com.example.steamtracker.model.Category
import com.example.steamtracker.model.ContentDescriptors
import com.example.steamtracker.model.Genre
import com.example.steamtracker.model.MetaCritic
import com.example.steamtracker.model.Platforms
import com.example.steamtracker.model.PriceOverview
import com.example.steamtracker.model.Rating
import com.example.steamtracker.model.Recommendations
import com.example.steamtracker.model.ReleaseDate
import com.example.steamtracker.model.Screenshot
import com.example.steamtracker.model.SteamSpyAppRequest
import com.example.steamtracker.model.SupportInfo
import com.example.steamtracker.model.SystemRequirements
import com.example.steamtracker.room.relations.AppNewsWithDetails
import com.example.steamtracker.ui.theme.SteamTrackerTheme
import com.example.steamtracker.utils.formatCurrency
import kotlinx.coroutines.flow.asFlow
import java.util.Locale

@Composable
fun AppPage(
    appDetails: AppDetails,
    appSpyInfo: SteamSpyAppRequest,
    newsAppsViewModel: NewsAppsViewModel,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth()
    ) {
        item {
            TitleCard(appDetails, appSpyInfo, modifier, contentPadding)
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
            GeneralInfo(appDetails, appSpyInfo, modifier, contentPadding)
        }

        item {
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.outlineVariant)
            ) {
                Genres(appDetails, appSpyInfo, modifier, contentPadding)
            }
        }

        item {
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primaryContainer)
            ) {
                ReviewScore(appDetails, appSpyInfo, modifier, contentPadding)
            }
        }

        item {
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.inversePrimary)
            ) {
                ShortInfo(appDetails, appSpyInfo, modifier, contentPadding)
            }
        }
    }
}

@Composable
fun TitleCard(
    appDetails: AppDetails,
    appSpyInfo: SteamSpyAppRequest,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
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
fun GeneralInfo(
    appDetails: AppDetails,
    appSpyInfo: SteamSpyAppRequest,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
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

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun Genres(
    appDetails: AppDetails,
    appSpyInfo: SteamSpyAppRequest,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    FlowRow(
        modifier = modifier.padding(12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Box(
            modifier = modifier.height(50.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Genres: ",
                fontWeight = FontWeight.Bold
            )
        }

        appDetails.genres?.forEach {
            Box(
                modifier = modifier.height(50.dp),
                contentAlignment = Alignment.Center
            ) {
                Genre(it.description)
            }
        }
    }
}

@Composable
fun ShortInfo(
    appDetails: AppDetails,
    appSpyInfo: SteamSpyAppRequest,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
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

@Composable
fun ReviewScore(
    appDetails: AppDetails,
    appSpyInfo: SteamSpyAppRequest,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
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
                modifier = modifier.padding(8.dp)
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

@Preview(showBackground = true)
@Composable
fun AppPagePreview() {
    SteamTrackerTheme {
        AppPage(
            appDetails = AppDetails(
                type = "0",
                name = "Video Game",
                steamAppId = 123123,
                requiredAge = 0,
                isFree = false,
                controllerSupport = "full",
                dlc = listOf(123, 123),
                detailedDescription = "Detailed game description",
                aboutTheGame = "About the game",
                shortDescription = "Short description",
                fullgame = null,
                supportedLanguages = "English",
                reviews = "Reviews",
                headerImage = "headerPath",
                capsuleImage = "capsulePath",
                capsuleImageV5 = "capsuleV5Path",
                website = "url",
                pcRequirements = SystemRequirements("minPC", "maxPC"),
                macRequirements = SystemRequirements("minMac", "maxMac"),
                linuxRequirements = SystemRequirements("minLinux", "maxLinux"),
                legalNotice = "legal",
                developers = listOf("Developer"),
                publishers = listOf("Publisher"),
                demos = null,
                priceOverview = PriceOverview(
                    currency = "USD",
                    initial = 4999,
                    final = 3999,
                    discountPercent = 20,
                    initialFormatted = "$49.99",
                    finalFormatted = "39.99"
                ),
                packages = listOf(123, 345),
                packageGroups = listOf(),
                platforms = Platforms(true, true, false),
                metacritic = MetaCritic(94, "Good"),
                categories = listOf(Category(123, "Category")),
                genres = listOf(Genre(123, "Genre"), Genre(123, "Genre")),
                screenshots = listOf(Screenshot(1, "path", "path")),
                movies = null,
                recommendations = Recommendations(123),
                achievements = AchievementsContainer(123, listOf(Achievement("Achievement", "path"))),
                releaseDate = ReleaseDate(false, "12-12-1212"),
                supportInfo = SupportInfo("path", "email"),
                background = "url",
                backgroundRaw = "url",
                contentDescriptors = ContentDescriptors(listOf(1,2,3), "notes"),
                ratings = mapOf("Positive" to Rating(
                    "rating",
                    descriptors = "descriptors",
                    requiredAge = "requiredAge",
                    useAgeGate = "useAgeGate",
                    interactiveElements = "interactiveElements"
                ))
            ),
            appSpyInfo = SteamSpyAppRequest(
                appid = 0,
                name = "VIDEO GAME",
                developer = "developer",
                publisher = "publisher",
                scoreRank = "0",
                positive = 123,
                negative = 345,
                userscore = 92,
                owners = "Many",
                averageForever = 123456,
                average2Weeks = 123,
                medianForever = 123456,
                median2Weeks = 123,
                price = "12.22",
                initialprice = "13.13",
                discount = "10",
                ccu = 1231,
                languages = "Languages",
                genre = "genre",
                tags = mapOf("tag" to 123 as Integer)
            ),
            newsAppsViewModel = NewsAppsViewModel(
                MockSteamworksRepository(
                    listOf(listOf<AppNewsWithDetails>()).asFlow(),
                    listOf(listOf<Int>()).asFlow()
                )
            ),
            modifier = Modifier,
            contentPadding = PaddingValues(0.dp)
        )
    }
}
