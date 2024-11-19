package com.eutech.pawprints.shared.presentation.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.eutech.pawprints.schedule.data.toPawPrintTime


import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.eutech.pawprints.schedule.data.Hours
import java.util.Calendar


@Composable
fun PawPrintTimePicker(
    modifier: Modifier = Modifier,
    label : String,
    value : String,
    onChange : (Hours) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    OutlinedTextField(
        shape = MaterialTheme.shapes.small,
        value = value,
        label = {
            Text(label)
        },
        onValueChange = {

        },
        readOnly = true,
        maxLines = 1,
        trailingIcon = {
            IconButton(onClick = {
                showDialog = !showDialog
            }) {
                Icon(
                    imageVector = Icons.Filled.Timer,
                    contentDescription = "Time"
                )
            }
        }
    )
    if (showDialog) {
        TimePickerDialog(
            onDismiss = {showDialog = !showDialog },
            onConfirm = {
                showDialog = false
                onChange(it)
            }
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerDialog(
    modifier: Modifier = Modifier,
    onConfirm: (Hours) -> Unit,
    onDismiss: () -> Unit,
) {
    // Get the current time from the system
    val currentTime = Calendar.getInstance()
    val timePickerState = rememberTimePickerState(
        initialHour = currentTime.get(Calendar.HOUR_OF_DAY),
        initialMinute = currentTime.get(Calendar.MINUTE),
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Pick a time") },
        text = {
            TimePicker(
                state = timePickerState,)
               },
            confirmButton = {
                TextButton(onClick = {
                    onConfirm(timePickerState.toPawPrintTime())
                }) {
                    Text("Confirm")
                }

            },
            dismissButton = {
                TextButton(
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.onErrorContainer
                    ),
                    onClick = onDismiss
                ) {
                    Text("Dismiss")
                }
            }
    )
}
