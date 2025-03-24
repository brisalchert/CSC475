package com.example.steamtracker.model

import com.google.gson.annotations.SerializedName

data class AppNewsRequest(
    val appnews: AppNews = AppNews()
)

data class AppNews(
    val appid: Int = 0,
    val newsitems: List<NewsItem> = listOf()
)

data class NewsItem(
    val appid: Int = 0,
    val gid: String = "",
    val title: String = "",
    val url: String = "",
    @SerializedName(value = "is_external_url")
    val isExternalUrl: Boolean = false,
    val author: String = "",
    val contents: String = "",
    val feedlabel: String = "",
    val date: Long = 0,
    val feedname: String = "",
    @SerializedName(value = "feed_type")
    val feedType: Int = 0
)
