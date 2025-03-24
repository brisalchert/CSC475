package com.example.steamtracker.model

import com.google.gson.annotations.SerializedName

data class AppNewsRequest(
    val appnews: AppNews
)

data class AppNews(
    val appid: Int,
    val newsitems: List<NewsItem>
)

data class NewsItem(
    val appid: Int,
    val gid: String,
    val title: String,
    val url: String,
    @SerializedName(value = "is_external_url")
    val isExternalUrl: Boolean,
    val author: String,
    val contents: String,
    val feedlabel: String,
    val date: Long,
    val feedname: String,
    @SerializedName(value = "feed_type")
    val feedType: Int
)
