package com.example.steamtracker.room.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "steam_spy_apps")
data class SteamSpyAppEntity (
    @PrimaryKey val appid: Int,
    val name: String,
    val developer: String,
    val publisher: String,
    val scoreRank: String,
    val positive: Int,
    val negative: Int,
    val userscore: Int,
    val owners: String,
    val averageForever: Int,
    val average2Weeks: Int,
    val medianForever: Int,
    val median2Weeks: Int,
    val price: String,
    val initialprice: String,
    val discount: String,
    val ccu: Int,
    val languages: String? = null,
    val genre: String? = null,
    val lastUpdated: Long
)

@Entity(
    primaryKeys = ["appid", "tagName"],
    foreignKeys = [
        ForeignKey(
            entity = SteamSpyAppEntity::class,
            parentColumns = ["appid"],
            childColumns = ["appid"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class TagEntity(
    val appid: Int,
    val tagName: String,
    val tagCount: Int
)
