package com.eutech.pawprints.pets.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun AddPetInfoDialog(
    modifier: Modifier = Modifier,
    isLoading : Boolean,
    onSave : (String, String) -> Unit
) {
    var dialogOpen by remember { mutableStateOf(false) }
    var label by remember { mutableStateOf("") }
    var value by remember { mutableStateOf("") }

    FilledIconButton(
        shape = MaterialTheme.shapes.small,
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ),
        enabled = !isLoading,
        onClick = {
            dialogOpen = true
        }
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Add Pet Info"
        )
    }

    if (dialogOpen) {
        AlertDialog(
            onDismissRequest = { dialogOpen = false },
            title = { Text(text = "Add Pet Info") },
            text = {
                Column(
                    modifier = modifier.width(400.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    TextField(
                        modifier = modifier.fillMaxWidth(),
                        value = label,
                        onValueChange = { label = it },
                        label = { Text(text = "Label") },
                        supportingText = {
                            Text("")
                        }

                    )

                    TextField(
                        modifier = modifier.fillMaxWidth(),
                        value = value,
                        onValueChange = { value = it },
                        label = { Text(text = "Value") },
                        supportingText = {
                            Text("")
                        }
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onSave(label, value)
                        label = ""
                        value =""
                        dialogOpen = false

                    }
                ) {
                    Text(text = "Save")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { dialogOpen = false }
                ) {
                    Text(text = "Cancel")
                }
            }
        )
    }
}
