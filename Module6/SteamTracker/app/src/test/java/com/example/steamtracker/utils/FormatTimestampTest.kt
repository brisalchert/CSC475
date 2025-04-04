package com.example.steamtracker.utils

import junit.framework.TestCase.assertEquals
import org.junit.Test

class FormatTimestampTest {
    @Test
    fun formatTimestamp_verifyTimestampFormat() {
        val april = 1743803251L
        val dateApril = "04-04-2025"
        val july = 1722376051L
        val dateJuly = "07-30-2024"

        assertEquals(
            dateApril,
            formatUnixTimestampSeconds(april)
        )
        assertEquals(
            dateJuly,
            formatUnixTimestampSeconds(july)
        )
    }
}
