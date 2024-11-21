package com.eutech.pawprints.users.tabs

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.eutech.pawprints.shared.presentation.utils.ErrorScreen
import com.eutech.pawprints.users.TransactionTabState
import com.eutech.pawprints.users.components.AppointmentTabCard
import com.eutech.pawprints.users.components.TransactionCard


@Composable
fun TransactionTab(
    modifier: Modifier = Modifier,
    transactionTabState: TransactionTabState
) {
    LazyColumn(
        modifier = modifier.fillMaxSize()
    ) {
        if (transactionTabState.isLoading) {
            item {
                LinearProgressIndicator(
                    modifier = modifier.fillMaxWidth()
                )
            }
        }
        if(transactionTabState.errors != null) {
            item {
                Box(
                    modifier = modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    ErrorScreen(
                        title = transactionTabState.errors
                    ) {
                    }
                }
            }
        }
        if (transactionTabState.errors == null && transactionTabState.transactions.isEmpty() &&
            !transactionTabState.isLoading) {
            item {
                Box(
                    modifier = modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("no transactions yet!")
                }
            }
        }
        items(transactionTabState.transactions) {
            TransactionCard(
                transaction = it
            )
        }
    }
}