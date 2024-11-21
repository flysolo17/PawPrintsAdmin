package com.eutech.pawprints.users.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem

import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.eutech.pawprints.R
import com.eutech.pawprints.R.drawable.paw_logo
import com.eutech.pawprints.shared.data.transactions.Transaction
import com.eutech.pawprints.shared.presentation.components.DetailRow
import com.eutech.pawprints.shared.presentation.utils.toPhp


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TransactionCard(
    modifier: Modifier = Modifier,
    transaction: Transaction
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(8.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text(
                text = "Status: ${transaction.status.name}",
                style = MaterialTheme.typography.labelSmall,
            )
            Column(
            ) {
                transaction.items.forEach { item ->
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
            }

            Spacer(modifier = Modifier.height(8.dp))
            DetailRow(
                label = "Total",
                value = "${transaction.payment?.total?.toPhp()}"
            )
        }
    }
}