package com.eutech.pawprints.transactions.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.eutech.pawprints.messages.MessageTimestamp
import com.eutech.pawprints.shared.data.transactions.Transaction
import com.eutech.pawprints.shared.presentation.utils.toPhp


@Composable
fun TransactionItem(
    modifier: Modifier = Modifier,
    transaction : Transaction,
    isSelected : Boolean,
    onClick : () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = if (isSelected) MaterialTheme.colorScheme.primary.copy(
                    alpha = 0.1f
                ) else MaterialTheme.colorScheme.surface,
            )
            .padding(8.dp)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = modifier.fillMaxWidth().weight(1f)
        ) {
            Text("${transaction.id}", style = MaterialTheme.typography.titleMedium)
            MessageTimestamp(date = transaction.createdAt)
        }
        Text(
            "${transaction.payment?.total?.toPhp()}",
            style = MaterialTheme.typography.titleMedium.copy(
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold
            )
        )
    }
}