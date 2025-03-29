package com.example.unitconverter

import com.example.unitconverter.logic.Length
import com.example.unitconverter.logic.Mass
import com.example.unitconverter.logic.Temperature
import com.example.unitconverter.logic.convertLength
import com.example.unitconverter.logic.convertMass
import com.example.unitconverter.logic.convertTemperature
import junit.framework.TestCase.assertEquals
import org.junit.Test
import kotlin.math.abs
import kotlin.math.floor
import kotlin.math.log10
import kotlin.math.pow

class ConversionUnitTest {
    // Helper function for floating point comparisons
    fun assertAlmostEquals(expected: Double, actual: Double) {
        // Calculate tolerance for 5 significant figures
        val power = floor(log10(abs(expected))).toInt() - 4
        val tolerance = 10.0.pow(power)

        assertEquals(expected, actual, tolerance)
    }

    // Format: (Fahrenheit, Celsius, Kelvin)
    val temperatures = listOf(
        listOf(-459.67, -273.15, 0.0),
        listOf(-40.0, -40.0, 233.15),
        listOf(32.0, 0.0, 273.15),
        listOf(212.0, 100.0, 373.15),
        listOf(-279.67, -173.15, 100.0),
        listOf(56.0, 13.3333, 286.483),
        listOf(12632.0, 7000.0, 7273.15),

        // Boundary cases (including invalid temperatures)
        listOf(-1800000457.9, -1000000272.1, -999999999.0),
        listOf(18000000030.0, 9999999999.0, 10000000272.0)
    )

    // Format: (Ounces, Pounds, Tons, Grams, Kilograms, MetricTons)
    val masses = listOf(
        listOf(1.0, 0.0625, 3.125e-5, 28.3495, 0.0283495, 2.83495e-5),
        listOf(16.0, 1.0, 0.0005, 453.592, 0.453592, 0.000453592),
        listOf(32000.0, 2000.0, 1.0, 907184.0, 907.184, 0.907184),
        listOf(0.035274, 0.00220462, 0.00000110231, 1.0, 0.001, 0.000001),
        listOf(35.274, 2.20462, 0.00110231, 1000.0, 1.0, 0.001),
        listOf(35274.0, 2204.62, 1.10231, 1000000.0, 1000.0, 1.0),

        // Boundary cases (including invalid masses)
        listOf(0.0, 0.0, 0.0, 0.0, 0.0, 0.0),
        listOf(
            -31999999968000.0, -1999999998000.0, -999999999.0,
            -907183999092816.0, -907183999093.0, -907183999.09
        ),
        listOf(
            319999999968000.0, 19999999998000.0, 9999999999.0,
            9071839999092816.0, 9071839999093.0, 9071839999.1
        )
    )

    // Format (Inches, Feet, Yards, Miles, Millimeters, Centimeters, Meters, Kilometers)
    val lengths = listOf(

        listOf(1.0, 0.0833333, 0.0277778, 0.0000157828, 25.4, 2.54, 0.0254, 0.0000254),
        listOf(12.0, 1.0, 0.333333, 0.000189394, 304.8, 30.48, 0.3048, 0.0003048),
        listOf(36.0, 3.0, 1.0, 0.000568182, 914.4, 91.44, 0.9144, 0.0009144),
        listOf(63360.0, 5280.0, 1760.0, 1.0, 1609340.0, 160934.0, 1609.34, 1.60934),
        listOf(0.0393701, 0.00328084, 0.00109361, 0.000000621371, 1.0, 0.1, 0.001, 0.000001),
        listOf(0.393701, 0.0328084, 0.0109361, 0.00000621371, 10.0, 1.0, 0.01, 0.00001),
        listOf(39.3701, 3.28084, 1.09361, 0.000621371, 1000.0, 100.0, 1.0, 0.001),
        listOf(39370.1, 3280.84, 1093.61, 0.621371, 1000000.0, 100000.0, 1000.0, 1.0),

        // Boundary cases (including invalid lengths)
        listOf(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0),
        listOf(
            -63359999936640.0, -5279999994720.0, -1759999998240.0, -999999999.0,
            -1609343998390656.0, -160934399839066.0, -1609343998391.0, -1609343998.4
        ),
        listOf(
            633599999936640.0, 52799999994720.0, 17599999998240.0, 9999999999.0,
            16093439998390656.0, 1609343999839066.0, 16093439998391.0, 16093439998.0
        )
    )

    @Test
    fun temperatureConversions_areCorrect() {
        for (temperatureList in temperatures) {
            for (i in 0..temperatureList.size - 1) {
                for (j in 0..temperatureList.size - 1) {
                    assertAlmostEquals(
                        temperatureList[i],
                        convertTemperature(
                            temperatureList[j],
                            Temperature.entries[j],
                            Temperature.entries[i]
                        )
                    )
                }
            }
        }
    }

    @Test
    fun massConversions_areCorrect() {
        for (massList in masses) {
            for (i in 0..massList.size - 1) {
                for (j in 0..massList.size - 1) {
                    assertAlmostEquals(
                        massList[i],
                        convertMass(
                            massList[j],
                            Mass.entries[j],
                            Mass.entries[i]
                        )
                    )
                }
            }
        }
    }

    @Test
    fun lengthConversions_areCorrect() {
        for (lengthList in lengths) {
            for (i in 0..lengthList.size - 1) {
                for (j in 0..lengthList.size - 1) {
                    assertAlmostEquals(
                        lengthList[i],
                        convertLength(
                            lengthList[j],
                            Length.entries[j],
                            Length.entries[i]
                        )
                    )
                }
            }
        }
    }
}
