package com.example.unitconverter.logic

/**
 * Enum class for temperature units
 */
enum class Temperature {
    Fahrenheit,
    Celsius,
    Kelvin
}

/**
 * Enum class for mass units
 */
enum class Mass {
    Ounces,
    Pounds,
    Tons,
    Grams,
    Kilograms,
    MetricTons
}

/**
 * Enum class for length units
 */
enum class Length {
    Inches,
    Feet,
    Yards,
    Miles,
    Millimeters,
    Centimeters,
    Meters,
    Kilometers
}

/**
 * Universal function for temperature conversions. Converts to an
 * intermediate Celsius value.
 */
fun convertTemperature(value: Double, from: Temperature, to: Temperature): Double {
    val celsius = when (from) {
        Temperature.Celsius -> value
        Temperature.Fahrenheit -> (value - 32) * (5.0/9.0)
        Temperature.Kelvin -> value + 273.15
    }

    return when (to) {
        Temperature.Celsius -> celsius
        Temperature.Fahrenheit -> celsius * (9.0/5.0) + 32
        Temperature.Kelvin -> celsius - 273.15
    }
}

/**
 * Universal function for mass conversions. Converts to an
 * intermediate pounds value with 6 significant figures.
 */
fun convertMass(value: Double, from: Mass, to: Mass): Double {
    val pounds = when (from) {
        Mass.Ounces -> value / 16.0
        Mass.Pounds -> value
        Mass.Tons -> value * 2000.0
        Mass.Grams -> value / 453.593
        Mass.Kilograms -> value * 2.20462
        Mass.MetricTons -> value * 2204.62
    }

    return when (to) {
        Mass.Ounces -> pounds * 16.0
        Mass.Pounds -> pounds
        Mass.Tons -> pounds / 2000.0
        Mass.Grams -> pounds * 453.593
        Mass.Kilograms -> pounds / 2.20462
        Mass.MetricTons -> pounds / 2204.62
    }
}

/**
 * Universal function for length conversions. Converts to an
 * intermediate meters value with 6 significant figures.
 */
fun convertLength(value: Double, from: Length, to: Length): Double {
    val meters = when (from) {
        Length.Inches -> value / 39.3701
        Length.Feet -> value / 3.28084
        Length.Yards -> value / 1.09361
        Length.Miles -> value * 1609.34
        Length.Millimeters -> value / 1000.0
        Length.Centimeters -> value / 100.0
        Length.Meters -> value
        Length.Kilometers -> value * 1000.0
    }

    return when (to) {
        Length.Inches -> meters * 39.3701
        Length.Feet -> meters * 3.28084
        Length.Yards -> meters * 1.09361
        Length.Miles -> meters / 1609.34
        Length.Millimeters -> meters * 1000.0
        Length.Centimeters -> meters * 100.0
        Length.Meters -> meters
        Length.Kilometers -> meters / 1000.0
    }
}
