package com.example.steamtracker.model

data class StoreSearchRequest(
    val total: Int = 0,
    val items: List<SearchAppInfo> = emptyList()
)
