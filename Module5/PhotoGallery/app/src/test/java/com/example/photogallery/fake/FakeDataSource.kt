package com.example.photogallery.fake

import com.example.photogallery.model.GalleryPhoto

object FakeDataSource {
    const val idOne = "img1"
    const val idTwo = "img2"
    const val imgOne = "url.1"
    const val imgTwo = "url.2"
    val photosList = listOf(
        GalleryPhoto(
            id = idOne,
            imgSrc = imgOne
        ),
        GalleryPhoto(
            id = idTwo,
            imgSrc = imgTwo
        )
    )
}