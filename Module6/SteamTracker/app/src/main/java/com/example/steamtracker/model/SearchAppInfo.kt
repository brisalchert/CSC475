package com.example.steamtracker.model

import com.google.gson.annotations.SerializedName

data class SearchAppInfo (
    val type: String,
    val name: String,
    val id: Int,
    val price: Price? = null,
    @SerializedName(value = "tiny_image")
    val tinyImage: String,
    val metascore: String,
    val platforms: Platforms,
    val streamingvideo: Boolean,
    @SerializedName(value = "controller_support")
    val controllerSupport: String? = null
)

data class Price(
    val currency: String,
    val initial: Int,
    val final: Int
)
