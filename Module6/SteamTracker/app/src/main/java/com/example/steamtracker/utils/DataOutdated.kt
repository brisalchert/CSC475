package com.example.steamtracker.utils

/**
 * Checks if the data in the database is out of date
 */
fun isDataOutdated(lastUpdated: Long?): Boolean {
    // No data exists in database
    if (lastUpdated == null) return true

    val lastUpdateDate = java.time.Instant.ofEpochMilli(lastUpdated)
        .atZone(java.time.ZoneId.systemDefault())
        .toLocalDate()

    val today = java.time.LocalDate.now()

    return lastUpdateDate.isBefore(today)
}
