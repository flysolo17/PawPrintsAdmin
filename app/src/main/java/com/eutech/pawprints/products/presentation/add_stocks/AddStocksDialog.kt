package com.eutech.pawprints.products.presentation.add_stocks

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.eutech.pawprints.shared.presentation.components.PawPrintDatePicker
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@Composable
fun AddStocksDialog(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    onConfirm: (Int, Date?) -> Unit
) {
    var quantity by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf<String>("") }


    AlertDialog(
        modifier = modifier,
        onDismissRequest = { onDismiss() },
        title = { Text("Add Stocks") },
        text = {
            Column {
                OutlinedTextField(
                    modifier = modifier.fillMaxWidth(),
                    value = quantity,
                    onValueChange = { value ->
                        if (value.all { it.isDigit() }) {
                            quantity = value
                        }
                    },
                    label = { Text("Quantity") },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(16.dp))
                PawPrintDatePicker(
                    label = "Expiration Date",
                    value = selectedDate,

                    onChange = {
                        selectedDate = it
                    },
                    modifier = modifier.weight(1f)
                )
            }
        },
        confirmButton = {
            TextButton(
                enabled = quantity.toIntOrNull() != null,
                onClick = {
                    val quantityInt = quantity.toIntOrNull() ?: 0
                    val expiry = try {
                        SimpleDateFormat("MMM, dd yyyy", Locale.getDefault()).parse(selectedDate)
                    } catch (e: Exception) {
                        null
                    }
                    onConfirm(quantityInt, expiry)
                }
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text("Cancel")
            }
        }
    )
}


