package com.example.photogallery.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GalleryPhoto(
    val id: String,
    @SerialName(value = "img_src")
    val imgSrc: String
)