package com.example.photogallery.fake

import com.example.photogallery.model.Data
import com.example.photogallery.model.RequestResult
import com.example.photogallery.model.Screenshot
import java.util.HashMap

object FakeDataSource {
    const val idOne = 1
    const val idTwo = 2
    const val pathThumbnailOne = "url.1"
    const val pathThumbnailTwo = "url.2"
    const val pathFullOne = "url.1.full"
    const val pathFullTwo = "url.2.full"
    val response = HashMap<String, RequestResult>()
    val gameIdsToNames = linkedMapOf(
        1 to "Game"
    )

    init {
        response["gameId"] = RequestResult(
            success = true,
            data = Data(
                listOf(
                    Screenshot(
                        id = idOne,
                        pathThumbnail = pathThumbnailOne,
                        pathFull = pathFullOne
                    ),
                    Screenshot(
                        id = idTwo,
                        pathThumbnail = pathThumbnailTwo,
                        pathFull = pathFullTwo
                    )
                )
            )
        )
    }
}