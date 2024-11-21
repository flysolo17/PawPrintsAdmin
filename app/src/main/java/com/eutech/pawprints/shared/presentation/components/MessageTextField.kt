package com.eutech.pawprints.shared.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@Composable
fun MessageTextField(
    modifier: Modifier = Modifier,
    isLoading : Boolean,
    message : String,
    onChange : (String) -> Unit,
    onSend : () -> Unit
) {
    TextField(
        modifier = modifier.fillMaxWidth().padding(
            vertical = 8.dp
        ),
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        value = message,
        placeholder = { Text("Enter your message") },
        onValueChange = {onChange(it)},
        trailingIcon = {
            IconButton(
                enabled = message.isNotEmpty() && !isLoading,
                onClick = { onSend() }
            ) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "send"
                )
            }
        }
    )
}