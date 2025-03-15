package com.example.photogallery.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RequestResult(
    val success: Boolean,
    val data: Data? = null
)

@Serializable
data class Data(
    val screenshots: List<Screenshot>
)

@Serializable
data class Screenshot(
    val id: Int,
    @SerialName(value = "path_thumbnail")
    val pathThumbnail: String,
    @SerialName(value = "path_full")
    val pathFull: String
)