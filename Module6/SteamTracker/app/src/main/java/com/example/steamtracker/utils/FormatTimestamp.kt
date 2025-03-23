package com.example.steamtracker.utils

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun formatUnixTimestampSeconds(timestamp: Long): String {
    val formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy")
    return Instant.ofEpochSecond(timestamp)
        .atZone(ZoneId.systemDefault())
        .format(formatter)
}
