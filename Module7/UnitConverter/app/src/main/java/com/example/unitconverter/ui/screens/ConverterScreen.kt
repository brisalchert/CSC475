package com.example.unitconverter.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import com.example.unitconverter.logic.Length
import com.example.unitconverter.logic.Mass
import com.example.unitconverter.logic.Temperature
import com.example.unitconverter.logic.convertLength
import com.example.unitconverter.logic.convertMass
import com.example.unitconverter.logic.convertTemperature
import com.example.unitconverter.ui.components.MeasurementSelector
import com.example.unitconverter.ui.components.UnitSelector
import com.example.unitconverter.ui.theme.UnitConverterTheme
import java.util.Locale

enum class Measurements {
    Temperature,
    Mass,
    Length
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConverterScreen(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    var selectedMeasurement by remember { mutableStateOf(Measurements.Temperature.name) }

    var unitTemperatureStarting by remember { mutableStateOf(Temperature.Fahrenheit.name) }
    var unitTemperatureEnding by remember { mutableStateOf(Temperature.Celsius.name) }

    var unitMassStarting by remember { mutableStateOf(Mass.Pounds.name) }
    var unitMassEnding by remember { mutableStateOf(Mass.Kilograms.name) }

    var unitLengthStarting by remember { mutableStateOf(Length.Feet.name) }
    var unitLengthEnding by remember { mutableStateOf(Length.Meters.name) }

    var valueStarting by remember { mutableStateOf("") }
    var valueEnding by remember { mutableStateOf("0.0") }

    fun updateConversion(input: String) {
        if (input == "" || input == "-") {
            valueEnding = "0.0"
        } else {
            val result = when (selectedMeasurement) {
                Measurements.Temperature.name -> convertTemperature(
                    value = input.toDouble(),
                    from = Temperature.entries.find { unitTemperatureStarting == it.name }!!,
                    to = Temperature.entries.find { unitTemperatureEnding == it.name }!!
                )

                Measurements.Mass.name -> convertMass(
                    value = input.toDouble(),
                    from = Mass.entries.find { unitMassStarting == it.name }!!,
                    to = Mass.entries.find { unitMassEnding == it.name }!!
                )

                Measurements.Length.name -> convertLength(
                    value = input.toDouble(),
                    from = Length.entries.find { unitLengthStarting == it.name }!!,
                    to = Length.entries.find { unitLengthEnding == it.name }!!
                )

                else -> "Error"
            }

            valueEnding = String.format(Locale.getDefault(), "%.5g", result)
        }
    }

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
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Use the selectors below to convert between units!",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(vertical = 16.dp, horizontal = 32.dp)
            )

            MeasurementSelector(
                selectedMeasurement = selectedMeasurement,
                onMeasurementSelect = { option ->
                    selectedMeasurement = option
                    valueStarting = ""
                    valueEnding = "0.0"
                },
                modifier = Modifier.padding(vertical = 32.dp)
            )

            Row(
                modifier = Modifier
                    .wrapContentWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = valueStarting,
                    onValueChange = { input ->
                        // Prevent input values that are too large
                        if (input.length > 10) return@TextField

                        // Ensure user input is a valid Double
                        if (input == "" || input == "-" ||
                            (input.toDoubleOrNull() != null && !input.endsWith('d'))) {
                            valueStarting = input.trim()

                            updateConversion(valueStarting)
                        }
                    },
                    placeholder = { Text("Enter value") },
                    singleLine = true,
                    modifier = Modifier.width(132.dp)
                )

                Text(
                    text = "="
                )

                TextField(
                    value = valueEnding,
                    onValueChange = {},
                    placeholder = { Text("Result") },
                    singleLine = true,
                    readOnly = true,
                    modifier = Modifier.width(132.dp)
                )
            }

            Row(
                modifier = Modifier.wrapContentWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Set unit options based on measurement selection
                val unitOptions: List<String> = when (selectedMeasurement) {
                    Measurements.Temperature.name -> Temperature.entries.map { it.name }
                    Measurements.Mass.name -> Mass.entries.map { it.name }
                    Measurements.Length.name -> Length.entries.map { it.name }
                    else -> emptyList()
                }

                // Set starting selections for selected measurements
                val (unitStarting, setUnitStarting: (String) -> Unit) = when (selectedMeasurement) {
                    Measurements.Temperature.name -> Pair(unitTemperatureStarting) { option: String ->
                        unitTemperatureStarting = option
                        updateConversion(valueStarting)
                    }
                    Measurements.Mass.name -> Pair(unitMassStarting) { option: String ->
                        unitMassStarting = option
                        updateConversion(valueStarting)
                    }
                    Measurements.Length.name -> Pair(unitLengthStarting) { option: String ->
                        unitLengthStarting = option
                        updateConversion(valueStarting)
                    }
                    else -> Pair("Error") { option:String -> }
                }

                // Set ending selections for selected measurements and recalculate valueEnding
                val (unitEnding, setUnitEnding: (String) -> Unit) = when (selectedMeasurement) {
                    Measurements.Temperature.name -> Pair(unitTemperatureEnding) { option: String ->
                        unitTemperatureEnding = option
                        updateConversion(valueStarting)
                    }
                    Measurements.Mass.name -> Pair(unitMassEnding) { option: String ->
                        unitMassEnding = option
                        updateConversion(valueStarting)
                    }
                    Measurements.Length.name -> Pair(unitLengthEnding) { option: String ->
                        unitLengthEnding = option
                        updateConversion(valueStarting)
                    }
                    else -> Pair("Error") { option: String -> }
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
