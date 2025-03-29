package com.example.unitconverter.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.unitconverter.ui.components.MeasurementSelector
import com.example.unitconverter.ui.components.UnitSelector
import com.example.unitconverter.ui.theme.UnitConverterTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConverterScreen(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    var selectedMeasurement by remember { mutableStateOf("Temperature") }

    var unitTemperatureStarting by remember { mutableStateOf("Fahrenheit") }
    var unitTemperatureEnding by remember { mutableStateOf("Celsius") }

    var unitWeightStarting by remember { mutableStateOf("Pounds") }
    var unitWeightEnding by remember { mutableStateOf("Kilogram") }

    var unitDistanceStarting by remember { mutableStateOf("Feet") }
    var unitDistanceEnding by remember { mutableStateOf("Meters") }

    Card(
        modifier = modifier
            .fillMaxSize()
            .padding(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceDim
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 128.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            Text(
                text = "Use the selectors below to convert between units!",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(32.dp)
            )

            MeasurementSelector(
                selectedMeasurement = selectedMeasurement,
                onMeasurementSelect = { option -> selectedMeasurement = option }
            )

            Row(
                modifier = modifier.wrapContentWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Set unit options based on measurement selection
                val unitOptions: List<String> = when (selectedMeasurement) {
                    "Temperature" -> listOf(
                        "Fahrenheit", "Celsius", "Kelvin"
                    )
                    "Weight" -> listOf("Ounces", "Pounds", "Tons", "Grams", "Kilograms", "Metric Tons"
                    )
                    "Distance" -> listOf("Inches", "Feet", "Yards", "Miles", "Millimeters", "Centimeters", "Meters", "Kilometers"
                    )
                    else -> emptyList()
                }

                // Set starting selections for selected measurements
                val (unitStarting, setUnitStarting: (String) -> Unit) = when (selectedMeasurement) {
                    "Temperature" -> Pair(unitTemperatureStarting) { option: String ->
                        unitTemperatureStarting = option
                    }
                    "Weight" -> Pair(unitWeightStarting) { option: String ->
                        unitWeightStarting = option
                    }
                    "Distance" -> Pair(unitDistanceStarting) { option: String ->
                        unitDistanceStarting = option
                    }
                    else -> Pair("Error") { option:String -> }
                }

                val (unitEnding, setUnitEnding: (String) -> Unit) = when (selectedMeasurement) {
                    "Temperature" -> Pair(unitTemperatureEnding) { option: String ->
                        unitTemperatureEnding = option
                    }
                    "Weight" -> Pair(unitWeightEnding) { option: String ->
                        unitWeightEnding = option
                    }
                    "Distance" -> Pair(unitDistanceEnding) { option: String ->
                        unitDistanceEnding = option
                    }
                    else -> Pair("Error") { option:String -> }
                }

                UnitSelector(
                    unitOptions = unitOptions,
                    selectedUnit = unitStarting,
                    onUnitSelected = setUnitStarting
                )

                Text(
                    text = "to"
                )

                UnitSelector(
                    unitOptions = unitOptions,
                    selectedUnit = unitEnding,
                    onUnitSelected = setUnitEnding
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ConverterScreenPreview() {
    UnitConverterTheme {
        ConverterScreen()
    }
}
