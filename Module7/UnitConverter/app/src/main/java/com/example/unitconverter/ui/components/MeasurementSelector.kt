package com.example.unitconverter.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.unitconverter.ui.theme.UnitConverterTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MeasurementSelector(
    selectedMeasurement: String,
    onMeasurementSelect: (String) -> Unit
) {
    val measurementOptions = listOf("Temperature", "Distance", "Weight")
    var measurementExpanded by remember { mutableStateOf(false) }
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        ExposedDropdownMenuBox(
            expanded = measurementExpanded,
            onExpandedChange = { measurementExpanded = !measurementExpanded }
        ) {
            OutlinedTextField(
                value = selectedMeasurement,
                onValueChange = {},
                readOnly = true,
                modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable, true),
                label = {
                    Text(
                        text = "Choose a measurement"
                    )
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                    unfocusedContainerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )

            ExposedDropdownMenu(
                expanded = measurementExpanded,
                onDismissRequest = { measurementExpanded = false },
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                shadowElevation = 8.dp,
            ) {
                measurementOptions.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            onMeasurementSelect(option)
                            measurementExpanded = false
                        }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MeasurementSelectorPreview() {
    UnitConverterTheme {
        MeasurementSelector(
            selectedMeasurement = "Selected Measurement",
            onMeasurementSelect = {}
        )
    }
}
