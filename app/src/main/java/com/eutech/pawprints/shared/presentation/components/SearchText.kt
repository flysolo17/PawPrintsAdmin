package com.eutech.pawprints.shared.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.eutech.pawprints.pets.PetEvents


@Composable
fun SearchText(
    modifier: Modifier = Modifier,
    label : String,
    value : String,
    onChange : (String) -> Unit,
) {
    TextField(
        shape = MaterialTheme.shapes.extraLarge,
        maxLines = 1,
        modifier = modifier.fillMaxWidth().padding(
            vertical = 8.dp
        ),
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            errorIndicatorColor = Color.Transparent
        ),
        value = value,
        onValueChange = {onChange(it)},
        trailingIcon = {
            if(value.isEmpty()) {
                Icon(imageVector = Icons.Filled.Search, contentDescription = "Search")
            } else {
                IconButton(onClick = {onChange("")}) {
                    Icon(imageVector = Icons.Filled.Clear, contentDescription = "Clear")
                }
            }

        },
        placeholder = { Text(label) }
    )
}