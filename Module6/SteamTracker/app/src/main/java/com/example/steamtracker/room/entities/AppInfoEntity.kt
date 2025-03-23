package com.example.steamtracker.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "app_info")
data class AppInfoEntity(
    @PrimaryKey val id: Int,
    val categoryId: String, // Foreign key for featured_categories table
    val type: Int,
    val name: String,
    val discounted: Boolean,
    val discountPercent: Int,
    val originalPrice: Int,
    val finalPrice: Int,
    val currency: String,
    val largeCapsuleImage: String,
    val smallCapsuleImage: String,
    val windowsAvailable: Boolean,
    val macAvailable: Boolean,
    val linuxAvailable: Boolean,
    val streamingVideoAvailable: Boolean,
    val discountExpiration: Long = -1, // Default values for optional parameters
    val headerImage: String,
    val headline: String? = null,
    val controllerSupport: String? = null,
    val purchasePackage: Int = -1
)
