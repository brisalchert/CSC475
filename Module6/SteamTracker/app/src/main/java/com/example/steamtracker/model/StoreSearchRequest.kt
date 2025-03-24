package com.example.steamtracker.model

data class StoreSearchRequest(
    val total: Int,
    val items: List<SearchAppInfo> = emptyList()
)
