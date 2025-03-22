package com.example.steamtracker.model

import com.google.gson.annotations.SerializedName

data class FeaturedGamesRequest(
    @SerializedName(value = "large_capsules")
    val largeCapsules: List<AppInfo>,
    @SerializedName(value = "featured_win")
    val featuredWin: List<AppInfo>,
    @SerializedName(value = "featured_mac")
    val featuredMac: List<AppInfo>,
    @SerializedName(value = "featured_linux")
    val featuredLinux: List<AppInfo>,
    val layout: String,
    val status: Int
)
