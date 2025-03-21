package com.example.steamtracker.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FeaturedGamesRequest(
    @SerialName(value = "large_capsules")
    val largeCapsules: List<FeaturedGame>,
    @SerialName(value = "featured_win")
    val featuredWin: List<FeaturedGame>,
    @SerialName(value = "featured_mac")
    val featuredMac: List<FeaturedGame>,
    @SerialName(value = "featured_linux")
    val featuredLinux: List<FeaturedGame>,
    val layout: String,
    val status: Int
)

@Serializable
data class FeaturedGame(
    val id: Int,
    val type: Int,
    val name: String,
    val discounted: Boolean,
    @SerialName(value = "discount_percent")
    val discountPercent: Int,
    @SerialName(value = "original_price")
    val originalPrice: Int?,
    @SerialName(value = "final_price")
    val finalPrice: Int,
    val currency: String,
    @SerialName(value = "large_capsule_image")
    val largeCapsuleImage: String,
    @SerialName(value = "small_capsule_image")
    val smallCapsuleImage: String,
    @SerialName(value = "windows_available")
    val windowsAvailable: Boolean,
    @SerialName(value = "mac_available")
    val macAvailable: Boolean,
    @SerialName(value = "linux_available")
    val linuxAvailable: Boolean,
    @SerialName(value = "streamingvideo_available")
    val streamingVideoAvailable: Boolean,
    @SerialName(value = "discount_expiration")
    val discountExpiration: Long = -1, // Default values for optional parameters
    @SerialName(value = "header_image")
    val headerImage: String,
    val headline: String? = null,
    @SerialName(value = "controller_support")
    val controllerSupport: String? = null,
    @SerialName(value = "purchase_package")
    val purchasePackage: Int = -1
)
