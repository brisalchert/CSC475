package com.example.steamtracker.model

import com.google.gson.annotations.SerializedName

data class SearchAppInfo (
    val type: String = "",
    val name: String = "Name",
    val id: Int = 0,
    val price: Price? = null,
    @SerializedName(value = "tiny_image")
    val tinyImage: String = "",
    val metascore: String = "",
    val platforms: Platforms = Platforms(),
    val streamingvideo: Boolean = false,
    @SerializedName(value = "controller_support")
    val controllerSupport: String? = null
)

data class Price(
    val currency: String = "USD",
    val initial: Int = 0,
    val final: Int = 0
)
