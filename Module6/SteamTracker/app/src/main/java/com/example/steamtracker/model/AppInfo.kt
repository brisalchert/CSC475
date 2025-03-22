package com.example.steamtracker.model

import com.google.gson.annotations.SerializedName

data class AppInfo(
    val id: Int,
    val type: Int,
    val name: String,
    val discounted: Boolean,
    @SerializedName(value = "discount_percent")
    val discountPercent: Int,
    @SerializedName(value = "original_price")
    val originalPrice: Int?,
    @SerializedName(value = "final_price")
    val finalPrice: Int,
    val currency: String,
    @SerializedName(value = "large_capsule_image")
    val largeCapsuleImage: String,
    @SerializedName(value = "small_capsule_image")
    val smallCapsuleImage: String,
    @SerializedName(value = "windows_available")
    val windowsAvailable: Boolean,
    @SerializedName(value = "mac_available")
    val macAvailable: Boolean,
    @SerializedName(value = "linux_available")
    val linuxAvailable: Boolean,
    @SerializedName(value = "streamingvideo_available")
    val streamingVideoAvailable: Boolean,
    @SerializedName(value = "discount_expiration")
    val discountExpiration: Long = -1, // Default values for optional parameters
    @SerializedName(value = "header_image")
    val headerImage: String,
    val headline: String? = null,
    @SerializedName(value = "controller_support")
    val controllerSupport: String? = null,
    @SerializedName(value = "purchase_package")
    val purchasePackage: Int = -1
)
