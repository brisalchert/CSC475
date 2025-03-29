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
import com.example.unitconverter.ui.components.TemperatureUnitSelector
import com.example.unitconverter.ui.theme.UnitConverterTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConverterScreen(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    var selectedMeasurement by remember { mutableStateOf("Temperature") }

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

            when (selectedMeasurement) {
                "Temperature" -> TemperatureConversion()
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

@Composable
fun TemperatureConversion(
    modifier: Modifier = Modifier
) {
    var unitStarting by remember { mutableStateOf("Fahrenheit") }
    var unitEnding by remember { mutableStateOf("Celsius") }

    Row(
        modifier = modifier.wrapContentWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TemperatureUnitSelector(
            selectedUnit = unitStarting,
            onUnitSelected = { option -> unitStarting = option }
        )

        Text(
            text = "to"
        )

        TemperatureUnitSelector(
            selectedUnit = unitEnding,
            onUnitSelected = { option -> unitEnding = option }
        )
    }
}
