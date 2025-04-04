package com.example.steamtracker.utils

import junit.framework.TestCase.assertEquals
import org.junit.Test
import java.util.Locale

class FormatCurrencyTest {
    @Test
    fun formatCurrency_verifyCurrencyFormat() {
        assertEquals(
            "$4.20",
            formatCurrency(4.2, Locale.US)
        )
        assertEquals(
            "$8.00",
            formatCurrency(8.0, Locale.US)
        )
        assertEquals(
            "$25.23",
            formatCurrency(25.23, Locale.US)
        )
        assertEquals(
            "$0.00",
            formatCurrency(0.0, Locale.US)
        )
        assertEquals(
            "$2,018.23",
            formatCurrency(2018.23, Locale.US)
        )
    }
}
