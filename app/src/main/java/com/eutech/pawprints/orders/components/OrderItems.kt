package com.eutech.pawprints.orders.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.eutech.pawprints.shared.data.transactions.PaymentStatus
import com.eutech.pawprints.shared.data.transactions.TransactionWithUser
import com.eutech.pawprints.shared.data.transactions.getColor
import com.eutech.pawprints.shared.data.transactions.getIcon
import com.eutech.pawprints.shared.presentation.utils.displayDate
import com.eutech.pawprints.shared.presentation.utils.toPhp


@Composable
fun OrderItems(
    modifier: Modifier = Modifier,
    transactionWithUser: TransactionWithUser,
    onEdit: () -> Unit,
    isUpdating: Boolean,
    onAddPayment: () -> Unit,
) {
    Row(
        modifier = modifier.fillMaxHeight(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = modifier
                .weight(.6f)
                .padding(8.dp),

            ) {
            Text("${transactionWithUser.transaction.id}", style = MaterialTheme.typography.titleSmall.copy(color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold))
        }
        Box(
            modifier = modifier
                .weight(1f)
                .padding(8.dp),

            ) {
            Text("${transactionWithUser.users?.name}", style = MaterialTheme.typography.titleSmall.copy(color = MaterialTheme.colorScheme.onSurface))
        }

        Box(
            modifier = modifier
                .weight(0.6f)
                .padding(8.dp)) {
            val payment = transactionWithUser.transaction.payment
            val status = payment?.status?.name.orEmpty()
            val total = payment?.total?.toPhp().orEmpty()
            Text(
                text = "$total • $status",
                style = MaterialTheme.typography.titleSmall.copy(
                    color = MaterialTheme.colorScheme.onSurface
                )
            )
        }
        Box(
            modifier = modifier
                .weight(0.5f)
                .padding(8.dp),
            ) {
            val color = transactionWithUser.transaction.status.getColor()
            val icon = transactionWithUser.transaction.status.getIcon()
            Row(
                modifier = modifier.wrapContentSize().background(
                    color = color,
                    shape = MaterialTheme.shapes.small
                ).padding(4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ){
                Icon(
                    imageVector = icon,
                    contentDescription ="null",
                    modifier = modifier.size(16.dp)
                )
                Text(
                    "${transactionWithUser.transaction.status}",
                    style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurface)
                )
            }

        }
        Box(
            modifier = modifier
                .weight(.8f)
                .padding(8.dp),

            ) {
            Text("${transactionWithUser.transaction.updatedAt.displayDate()}", style = MaterialTheme.typography.titleSmall.copy(color = MaterialTheme.colorScheme.onSurface))
        }
        Row(
            modifier = modifier
                .weight(1f)
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
            if (transactionWithUser.transaction?.payment?.status != PaymentStatus.PAID) {
                OutlinedButton(
                    enabled = !isUpdating,
                    onClick = {onAddPayment()}
                ) { Text("Add Payment") }
            }

            Button(
                enabled = !isUpdating,
                onClick = {onEdit()}
            ) { Text("Edit") }
        }
    }
}