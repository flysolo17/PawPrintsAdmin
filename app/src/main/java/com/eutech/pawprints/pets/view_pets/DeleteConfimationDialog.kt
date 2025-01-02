package com.eutech.pawprints.pets.view_pets

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color


@Composable
fun DeleteConfirmationDialog(
    modifier: Modifier = Modifier,
    title : String,
    body : String,
    onConfirm : () -> Unit
) {
    var isShow by remember { mutableStateOf(false) }
    if (isShow) {
        AlertDialog(
            onDismissRequest = { isShow = false },
            title = {
                Text(text = title, style = MaterialTheme.typography.titleMedium)
            },
            text = {
                Text(text = body, style = MaterialTheme.typography.bodyMedium)
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onConfirm()
                        isShow = false // Close the dialog after confirmation
                    }
                ) {
                    Text("Confirm", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { isShow = false }
                ) {
                    Text("Cancel")
                }
            },
            modifier = modifier
        )
    }
    IconButton(
        onClick = {isShow = !isShow}
    ) {
        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = "Delete",
            tint = MaterialTheme.colorScheme.error
        )
    }
}