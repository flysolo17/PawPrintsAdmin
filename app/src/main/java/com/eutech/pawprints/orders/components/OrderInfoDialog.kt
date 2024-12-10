package com.eutech.pawprints.orders.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.ListItem
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.eutech.pawprints.R.drawable.paw_logo
import com.eutech.pawprints.appointments.data.appointment.AppointmentStatus
import com.eutech.pawprints.appointments.data.appointment.getColor
import com.eutech.pawprints.appointments.data.appointment.getIcon
import com.eutech.pawprints.home.presentation.components.AppointmentInfo
import com.eutech.pawprints.home.presentation.components.UserCardInfo
import com.eutech.pawprints.shared.data.transactions.TransactionStatus
import com.eutech.pawprints.shared.data.transactions.TransactionWithUser
import com.eutech.pawprints.shared.data.transactions.getColor
import com.eutech.pawprints.shared.data.transactions.getIcon
import com.eutech.pawprints.shared.presentation.components.DetailRow
import com.eutech.pawprints.shared.presentation.utils.displayDate
import com.eutech.pawprints.shared.presentation.utils.toPhp
import com.eutech.pawprints.transactions.components.OrderTrackerItem


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun OrderInfoDialog(
    modifier: Modifier = Modifier,
    transactionWithUser: TransactionWithUser,
    onDismiss : () -> Unit,
    onConfirm : (TransactionStatus) -> Unit,
) {
    val transaction = transactionWithUser.transaction
    val user = transactionWithUser.users
    var expanded by remember { mutableStateOf(false) }
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = modifier
                .fillMaxWidth(0.8f) // 80% of the screen width
                .wrapContentHeight()
                .padding(16.dp),
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.background,
            ) {
                LazyVerticalGrid(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    columns = GridCells.Fixed(2),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(4.dp)
                ) {
                    item(span = { GridItemSpan(2) }) {
                        Text(
                            "${transaction.id}",
                            style = MaterialTheme.typography.titleLarge,
                            modifier = modifier.fillMaxWidth().padding(bottom = 12.dp)
                        )
                    }
                    item(span = { GridItemSpan(1) }) {
                        AppointmentInfo(title = "Transaction Details") {
                            DetailRow(
                                label = "Location",
                                value = "Aucena Veterinary Clinic",
                                hasDivider = false
                            )

                            DetailRow(
                                label = "Transaction Date",
                                value = "${transaction.createdAt.displayDate()}",
                                hasDivider = false
                            )
                            DetailRow(
                                label = "Last Updated",
                                value = "${transaction.updatedAt.displayDate()}",
                                hasDivider = false
                            )
                            DetailRow(
                                label = "Type",
                                value = "${transaction.type.name}",
                                hasDivider = false
                            )
                            DetailRow(
                                label = "Status",
                                value = "${transaction.status.name}",
                                hasDivider = false
                            )
                        }
                    }
                    item(span = { GridItemSpan(1) }) {
                        Column {
                            AppointmentInfo(title = "Payment Details") {
                                DetailRow(
                                    label = "Status",
                                    value = "${transaction.payment?.status?.name}",
                                    hasDivider = false
                                )
                                DetailRow(
                                    label = "Type",
                                    value = "${transaction.payment?.type?.name}",
                                    hasDivider = false
                                )
                                DetailRow(
                                    label = "Total Amount",
                                    value = "${transaction.payment?.total?.toPhp()}",
                                    hasDivider = false
                                )
                            }

                            AppointmentInfo(
                                title = "User Details"
                            ) {
                                if (user != null) {
                                    UserCardInfo(users = user)
                                } else {
                                    Text("no user data")
                                }
                            }
                        }
                    }
                    item(span = { GridItemSpan(2) }) {
                        HorizontalDivider()
                    }
                    item(span = { GridItemSpan(2) }) {
                        Text("Items")
                    }
                    items(transaction.items,span = { GridItemSpan(2) }) { item->
                        ListItem(
                            text = { Text("${item.name.toString()}") },
                            icon = {
                                AsyncImage(
                                    model = item.imageUrl!!,
                                    contentDescription = item.name,
                                    error = painterResource(paw_logo),
                                    placeholder = painterResource(paw_logo),
                                    contentScale = ContentScale.Crop,
                                    modifier = modifier
                                        .size(60.dp)
                                        .clip(
                                            RoundedCornerShape(
                                                bottomEnd = 16.dp,
                                                bottomStart = 16.dp
                                            )
                                        )
                                )
                            },
                            secondaryText = {
                                Row(
                                    modifier = modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ){
                                    Text(
                                        text = ((item.quantity ?: 0) * (item.price ?: 0.0)).toPhp(),
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.secondary
                                    )
                                    Text(
                                        text = "${item.quantity}",
                                        style = MaterialTheme.typography.labelSmall
                                    )
                                }
                            }
                        )
                    }

                    item(
                        span = { GridItemSpan(2) }
                    ) {
                        Row(
                            modifier = modifier.fillMaxWidth().padding(8.dp),
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            TextButton(onClick = onDismiss) {
                                Text("Close")
                            }
                            val statuses = TransactionStatus.entries.toTypedArray()
                            Box(
                                modifier = Modifier.width(200.dp)
                            ) {
                                OutlinedButton(
                                    onClick = { expanded = !expanded },
                                    shape = MaterialTheme.shapes.small,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(text = "Edit Status")
                                }
                                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                                    statuses.forEach { status ->
                                        DropdownMenuItem(
                                            leadingIcon = {
                                                val icon = status.getIcon()
                                                val color = status.getColor()
                                                Box(
                                                    modifier = modifier
                                                        .wrapContentSize()
                                                        .background(
                                                            color = color,
                                                            shape = CircleShape
                                                        )
                                                ){
                                                    Icon(
                                                        imageVector = icon,
                                                        contentDescription = status.name,
                                                        modifier = modifier
                                                            .size(24.dp)
                                                            .padding(4.dp),
                                                        tint = Color.White
                                                    )
                                                }
                                            },
                                            text = { Text(status.name) },
                                            onClick = {
                                                onConfirm(status)
                                                expanded = false
                                            }
                                        )
                                    }
                                }
                            }

                        }
                    }
                }
        }
    }
}