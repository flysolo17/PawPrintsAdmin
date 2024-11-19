package com.eutech.pawprints.appointments.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTimeFilled
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.eutech.pawprints.schedule.data.Hours
import com.eutech.pawprints.schedule.data.display
import java.util.Calendar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PawPrintTimePicker(
    modifier: Modifier = Modifier,
    hours : Hours,
    label : String,
    onConfirm: (TimePickerState) -> Unit,

    ) {
    var dialog by remember {
        mutableStateOf(false)
    }
    val currentTime = Calendar.getInstance()
    val timePickerState = rememberTimePickerState(
        initialHour = currentTime.get(Calendar.HOUR_OF_DAY),
        initialMinute = currentTime.get(Calendar.MINUTE),
        is24Hour = true,
    )

    if (dialog) {
        TimePickerDialog(
            onDismiss = { dialog  = !dialog },
            onConfirm = {
                onConfirm(timePickerState)
                dialog  = !dialog
            }
        ) {
            TimePicker(
                state = timePickerState,
            )
        }
    }

    TextField(
        modifier = modifier.fillMaxWidth(),
        value = hours.display(),
        onValueChange = {},
        readOnly = true,
        trailingIcon = {
            IconButton(onClick = { dialog = true }) {
                Icon(imageVector = Icons.Default.AccessTimeFilled, contentDescription = "Calendar")
            }
        },
        label = { Text(label) },
    )

}

@Composable
fun TimePickerDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    content: @Composable () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text("Dismiss")
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm() }) {
                Text("OK")
            }
        },
        text = { content() }
    )
}