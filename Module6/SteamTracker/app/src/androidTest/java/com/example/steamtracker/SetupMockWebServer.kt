package com.example.steamtracker

import androidx.test.platform.app.InstrumentationRegistry
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import java.io.InputStreamReader
import javax.net.ssl.SSLSocketFactory

fun setupMockWebServer(mockWebServer: MockWebServer, sslSocketFactory: SSLSocketFactory) {
    // Set up server for HTTPS
    mockWebServer.useHttps(sslSocketFactory, false)

    mockWebServer.dispatcher = object : Dispatcher() {
        override fun dispatch(request: RecordedRequest): MockResponse {
            val path = request.path ?: ""
            return when {
                path == "/featuredcategories?cc=us&l=en" -> {
                    MockResponse()
                        .setResponseCode(200)
                        .setBody(loadJsonFromResources("featured_categories.json"))
                }
                path == "/appdetails?cc=us&l=en&appids=1174180" -> {
                    MockResponse()
                        .setResponseCode(200)
                        .setBody(loadJsonFromResources("appdetails_1174180.json"))
                }
                path == "/api.php?request=appdetails&appid=1174180" -> {
                    MockResponse()
                        .setResponseCode(200)
                        .setBody(loadJsonFromResources("spydetails_1174180.json"))
                }
                path == "/api.php?request=all&page=0" -> {
                    MockResponse()
                        .setResponseCode(200)
                        .setBody(loadJsonFromResources("spydetails_all.json"))
                }
                path == "/appdetails?cc=us&l=en&appids=275850" -> {
                    MockResponse()
                        .setResponseCode(200)
                        .setBody(loadJsonFromResources("appdetails_275850.json"))
                }
                path == "/api.php?request=appdetails&appid=275850" -> {
                    MockResponse()
                        .setResponseCode(200)
                        .setBody(loadJsonFromResources("spydetails_275850.json"))
                }
                path == "/appdetails?cc=us&l=en&appids=601150" -> {
                    MockResponse()
                        .setResponseCode(200)
                        .setBody(loadJsonFromResources("appdetails_601150.json"))
                }
                path == "/api.php?request=appdetails&appid=601150" -> {
                    MockResponse()
                        .setResponseCode(200)
                        .setBody(loadJsonFromResources("spydetails_601150.json"))
                }
                path == "/storesearch/?cc=us&l=en&term=Elden" -> {
                    MockResponse()
                        .setResponseCode(200)
                        .setBody(loadJsonFromResources("storesearch_Elden.json"))
                }
                path == "/appdetails?cc=us&l=en&appids=1245620" -> {
                    MockResponse()
                        .setResponseCode(200)
                        .setBody(loadJsonFromResources("appdetails_1245620.json"))
                }
                path == "/api.php?request=appdetails&appid=1245620" -> {
                    MockResponse()
                        .setResponseCode(200)
                        .setBody(loadJsonFromResources("spydetails_1245620.json"))
                }
                path == "/storesearch/?cc=us&l=en&term=Dark%20Souls" -> {
                    MockResponse()
                        .setResponseCode(200)
                        .setBody(loadJsonFromResources("storesearch_Dark_Souls.json"))
                }
                path == "/appdetails?cc=us&l=en&appids=374320" -> {
                    MockResponse()
                        .setResponseCode(200)
                        .setBody(loadJsonFromResources("appdetails_374320.json"))
                }
                path == "/api.php?request=appdetails&appid=374320" -> {
                    MockResponse()
                        .setResponseCode(200)
                        .setBody(loadJsonFromResources("spydetails_374320.json"))
                }
                path == "/storesearch/?cc=us&l=en&term=Elden%20Ring" -> {
                    MockResponse()
                        .setResponseCode(200)
                        .setBody(loadJsonFromResources("storesearch_Elden_Ring.json"))
                }
                path == "/storesearch/?cc=us&l=en&term=Split%20Fiction" -> {
                    MockResponse()
                        .setResponseCode(200)
                        .setBody(loadJsonFromResources("storesearch_Split_Fiction.json"))
                }
                path == "/appdetails?cc=us&l=en&appids=2001120" -> {
                    MockResponse()
                        .setResponseCode(200)
                        .setBody(loadJsonFromResources("appdetails_2001120.json"))
                }
                path == "/api.php?request=appdetails&appid=2001120" -> {
                    MockResponse()
                        .setResponseCode(200)
                        .setBody(loadJsonFromResources("spydetails_2001120.json"))
                }
                path == "/appdetails?cc=us&l=en&appids=323190" -> {
                    MockResponse()
                        .setResponseCode(200)
                        .setBody(loadJsonFromResources("appdetails_323190.json"))
                }
                path == "/api.php?request=appdetails&appid=323190" -> {
                    MockResponse()
                        .setResponseCode(200)
                        .setBody(loadJsonFromResources("spydetails_323190.json"))
                }
                path == "/appdetails?cc=us&l=en&appids=3540920" -> {
                    MockResponse()
                        .setResponseCode(200)
                        .setBody(loadJsonFromResources("appdetails_3540920.json"))
                }
                path == "/api.php?request=appdetails&appid=3540920" -> {
                    MockResponse()
                        .setResponseCode(200)
                        .setBody(loadJsonFromResources("spydetails_3540920.json"))
                }
                path == "/ISteamNews/GetNewsForApp/v2/?feeds=steam_community_announcements&appid=2001120" -> {
                    MockResponse()
                        .setResponseCode(200)
                        .setBody(loadJsonFromResources("appnews_2001120.json"))
                }
                path.contains("/appdetails?cc=us&l=en&appids=") == true -> {
                    MockResponse()
                        .setResponseCode(200)
                        .setBody(loadJsonFromResources("appdetails_3900.json"))
                }
                path.contains("/api.php?request=appdetails&appid=") == true -> {
                    MockResponse()
                        .setResponseCode(200)
                        .setBody(loadJsonFromResources("spydetails_-1.json"))
                }
                else -> MockResponse().setResponseCode(404)
            }
        }
    }
}

fun loadJsonFromResources(fileName: String): String {
    val context = InstrumentationRegistry.getInstrumentation().context
    val inputStream = context.assets.open(fileName)
    val reader = InputStreamReader(inputStream)
    return reader.readText()
}
