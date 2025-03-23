package com.example.steamtracker.utils

import java.text.NumberFormat
import java.util.Locale

fun formatCurrency(amount: Double, locale: Locale = Locale.US): String {
    val formatter = NumberFormat.getCurrencyInstance(locale)
    return formatter.format(amount)
}
