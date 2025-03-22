package com.example.steamtracker.model

import com.google.gson.annotations.SerializedName

data class SteamSpyAppRequest(
    val appid: Int,
    val name: String,
    val developer: String,
    val publisher: String,
    @SerializedName(value = "score_rank")
    val scoreRank: String,
    val positive: Int,
    val negative: Int,
    val userscore: Int,
    val owners: String,
    @SerializedName(value = "average_forever")
    val averageForever: Int,
    @SerializedName(value = "average_2weeks")
    val average2Weeks: Int,
    @SerializedName(value = "median_forever")
    val medianForever: Int,
    @SerializedName(value = "median_2weeks")
    val median2Weeks: Int,
    val price: String,
    val initialprice: String,
    val discount: String,
    val ccu: Int,
    val languages: String? = null,
    val genre: String? = null,
    val tags: Map<String, Integer>? = null
)
