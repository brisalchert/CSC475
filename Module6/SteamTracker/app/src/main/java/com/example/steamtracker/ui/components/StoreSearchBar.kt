package com.example.steamtracker.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.steamtracker.ui.theme.SteamTrackerTheme

@Composable
fun StoreSearchBar(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    Card(
        modifier = modifier
            .wrapContentSize()
            .padding(10.dp)
            .zIndex(1f),
        colors = CardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            disabledContainerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            disabledContentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    ) {
        val query = remember { mutableStateOf("") }

        TextField(
            value = query.value,
            onValueChange = { query.value = it },
            placeholder = { Text("Search the Steam Store") },
            singleLine = true,
            leadingIcon = {
                Icon(
                    Icons.Filled.Menu,
                    contentDescription = "Menu Icon",
                    modifier = modifier.padding(PaddingValues(10.dp, 0.dp, 0.dp, 0.dp))
                )
            },
            trailingIcon = {
                if (query.value.isNotEmpty()) {
                    IconButton(onClick = { query.value = "" }) {
                        Icon(Icons.Filled.Close, contentDescription = "Clear Query")
                    }
                } else {
                    Icon(Icons.Filled.Search, contentDescription = null)
                }
            },
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.primaryContainer
            ),
            modifier = modifier
                .fillMaxWidth()
        )

        // TODO: Add list of autocomplete results
        if (query.value.isNotEmpty()) {
            Column(modifier = modifier.height(200.dp)) {}
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StoreSearchBarPreview() {
    SteamTrackerTheme {
        StoreSearchBar()
    }
}
