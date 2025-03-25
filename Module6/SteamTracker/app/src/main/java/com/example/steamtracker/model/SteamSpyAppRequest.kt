package com.example.steamtracker.model

import com.google.gson.annotations.SerializedName

data class SteamSpyAppRequest(
    val appid: Int = 0,
    val name: String? = "Name",
    val developer: String = "Developer",
    val publisher: String = "Publisher",
    @SerializedName(value = "score_rank")
    val scoreRank: String = "",
    val positive: Int = 0,
    val negative: Int = 0,
    val userscore: Int = 0,
    val owners: String = "",
    @SerializedName(value = "average_forever")
    val averageForever: Int = 0,
    @SerializedName(value = "average_2weeks")
    val average2Weeks: Int = 0,
    @SerializedName(value = "median_forever")
    val medianForever: Int = 0,
    @SerializedName(value = "median_2weeks")
    val median2Weeks: Int = 0,
    val price: String? = null,
    val initialprice: String? = null,
    val discount: String? = null,
    val ccu: Int = 0,
    val languages: String? = null,
    val genre: String? = null,
    val tags: Map<String, Int>? = null
)
