package com.example.steamtracker.utils

import org.junit.Test
import java.util.Calendar
import java.util.TimeZone

class DataOutdatedTest {
    @Test
    fun isDataOutdated_verifyOutdatedStatus() {
        val today = System.currentTimeMillis()
        val startOfDay = getStartOfDayMillis()
        val yesterday = today - (1000L * 60L * 60L * 24L)
        val twoDaysAgo = today - (1000L * 60L * 60L * 24L * 2L)

        assert(!isDataOutdated(today))
        assert(!isDataOutdated(startOfDay))
        assert(isDataOutdated(yesterday))
        assert(isDataOutdated(twoDaysAgo))
    }

    fun getStartOfDayMillis(): Long {
        val calendar = Calendar.getInstance(TimeZone.getDefault())
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }
}
