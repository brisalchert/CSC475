package com.example.steamtracker.network

import com.example.steamtracker.model.AppNewsRequest
import retrofit2.http.GET
import retrofit2.http.Query

interface SteamworksApiService {
    @GET("ISteamNews/GetNewsForApp/v2/?feeds=steam_community_announcements")
    suspend fun getAppNews(@Query("appid") appId: Int): AppNewsRequest
}
