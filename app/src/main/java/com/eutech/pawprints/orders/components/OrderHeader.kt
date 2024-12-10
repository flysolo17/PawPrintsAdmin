package com.eutech.pawprints.orders.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.eutech.pawprints.shared.presentation.components.SearchText


@Composable
fun OrderHeader(
    modifier: Modifier = Modifier,
    searchText : String,
    onSearching : (String) -> Unit,
    onBackPress : () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.primary
            )
    ) {
        Row(
            modifier = modifier.fillMaxWidth().padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(
                    onClick = {onBackPress()}
                ) { Icon(Icons.Filled.ArrowBack, contentDescription = "back", tint = MaterialTheme.colorScheme.onPrimary) }
                Text("Orders", style = MaterialTheme.typography.titleLarge.copy(
                    color = MaterialTheme.colorScheme.onPrimary
                ))
            }
            SearchText(
                modifier = modifier.width(400.dp),
                label = "Search Transaction here..",
                value = searchText,
                onChange = {onSearching(it)}
            )
        }

        Row(
            modifier = modifier.fillMaxHeight()
        ) {
            Box(
                modifier = modifier
                    .weight(0.6f)
                    .padding(8.dp),
                ) {
                Text("ID", style = MaterialTheme.typography.titleMedium.copy(color = MaterialTheme.colorScheme.onPrimary))
            }
            Box(
                modifier = modifier
                    .weight(1f)
                    .padding(8.dp),
                ) {
                Text("ORDERED BY", style = MaterialTheme.typography.titleMedium.copy(color = MaterialTheme.colorScheme.onPrimary))
            }
            Box(
                modifier = modifier
                    .weight(0.6f)
                    .padding(8.dp)) {

                Text("ORDER VALUE",
                    style = MaterialTheme.typography.titleMedium.copy(color = MaterialTheme.colorScheme.onPrimary)
                )
            }
            Box(
                modifier = modifier
                    .weight(0.5f)
                    .padding(8.dp),

                ) {
                Text("STATUS", style = MaterialTheme.typography.titleMedium.copy(color = MaterialTheme.colorScheme.onPrimary))
            }
            Box(
                modifier = modifier
                    .weight(.8f)
                    .padding(8.dp),

                ) {
                Text("LAST UPDATED", style = MaterialTheme.typography.titleMedium.copy(color = MaterialTheme.colorScheme.onPrimary))
            }
            Box(
                modifier = modifier
                    .weight(1f)
                    .padding(8.dp),
                ) { Text("ACTIONS", style = MaterialTheme.typography.titleMedium.copy(color = MaterialTheme.colorScheme.onPrimary)) }
        }
    }
}