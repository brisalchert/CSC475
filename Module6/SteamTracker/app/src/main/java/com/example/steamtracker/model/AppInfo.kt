package com.example.steamtracker.model

import com.google.gson.annotations.SerializedName

data class AppInfo(
    val id: Int = 0,
    val type: Int = 0,
    val name: String = "Name",
    val discounted: Boolean = false,
    @SerializedName(value = "discount_percent")
    val discountPercent: Int = 0,
    @SerializedName(value = "original_price")
    val originalPrice: Int = 0,
    @SerializedName(value = "final_price")
    val finalPrice: Int = 0,
    val currency: String = "USD",
    @SerializedName(value = "large_capsule_image")
    val largeCapsuleImage: String = "",
    @SerializedName(value = "small_capsule_image")
    val smallCapsuleImage: String = "",
    @SerializedName(value = "windows_available")
    val windowsAvailable: Boolean = false,
    @SerializedName(value = "mac_available")
    val macAvailable: Boolean = false,
    @SerializedName(value = "linux_available")
    val linuxAvailable: Boolean = false,
    @SerializedName(value = "streamingvideo_available")
    val streamingVideoAvailable: Boolean = false,
    @SerializedName(value = "discount_expiration")
    val discountExpiration: Long = -1, // Default values for optional parameters
    @SerializedName(value = "header_image")
    val headerImage: String = "",
    val headline: String? = null,
    @SerializedName(value = "controller_support")
    val controllerSupport: String? = null,
    @SerializedName(value = "purchase_package")
    val purchasePackage: Int = -1
)
