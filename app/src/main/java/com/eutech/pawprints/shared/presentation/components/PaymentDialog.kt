package com.eutech.pawprints.shared.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.eutech.pawprints.shared.presentation.utils.toPhp


@Composable
fun PaymentDialog(
    modifier: Modifier = Modifier,
    amount: Double,
    amountReceived: String,
    onChange: (String) -> Unit,
    onConfirm: (Double) -> Unit,
    onDismiss: () -> Unit,
    isPaying: Boolean
) {
    val received = amountReceived.toDoubleOrNull()
    val receivedText = received?.toPhp() ?: "Invalid"
    val change = (received ?: 0.00) - amount

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = { onConfirm(received ?: 0.00) },
                enabled = received != null && received >= amount && !isPaying
            ) {
                if (isPaying) {
                    CircularProgressIndicator(modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
                } else {
                    Text("Confirm")
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        title = {
            Text("Payment", style = MaterialTheme.typography.titleLarge)
        },
        text = {
            Column(modifier = modifier.fillMaxWidth().padding(12.dp)) {
                DetailRow(
                    label = "Amount",
                    value = "${amount.toPhp()}",
                    hasDivider = false
                )
                DetailRow(
                    label = "Amount Received",
                    value = receivedText,
                    hasDivider = false
                )
                DetailRow(
                    label = "Change",
                    value = "${change}",
                    hasDivider = false
                )

                Spacer(modifier = Modifier.height(8.dp))

                TextField(
                    value = amountReceived,
                    onValueChange = onChange,
                    modifier = Modifier.fillMaxWidth(),
                    isError = received == null,
                    label = { Text("Enter Amount Received") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    supportingText = {
                        if (received == null) {
                            Text(
                                "Invalid amount",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = MaterialTheme.colorScheme.error
                                )
                            )
                        }
                    }
                )
            }
        },
        modifier = Modifier.wrapContentSize().padding(16.dp),
        shape = RoundedCornerShape(12.dp),
        containerColor = MaterialTheme.colorScheme.background
    )
}
