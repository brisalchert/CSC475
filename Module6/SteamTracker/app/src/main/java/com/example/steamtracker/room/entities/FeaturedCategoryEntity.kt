package com.example.steamtracker.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "featured_categories")
data class FeaturedCategoryEntity(
    @PrimaryKey val id: String,
    val name: String,
    val type: String,
    val status: Int
)

@Entity(tableName = "spotlight_items")
data class SpotlightItemEntity(
    @PrimaryKey val name: String,
    val categoryId: String, // Foreign key for featured_categories table
    val headerImage: String,
    val body: String,
    val url: String
)
