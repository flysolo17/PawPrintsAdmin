package com.eutech.pawprints.transactions

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.eutech.pawprints.shared.presentation.components.SearchText
import com.eutech.pawprints.shared.presentation.utils.toast
import com.eutech.pawprints.transactions.components.SelectedTransactionCard
import com.eutech.pawprints.transactions.components.TransactionItem


@Composable
fun TransactionScreen(
    modifier: Modifier = Modifier,
    state: TransactionsState,
    events: (TransactionEvents) -> Unit,
    navHostController : NavHostController,
) {
    val context = LocalContext.current
    LaunchedEffect(state.selectedTransaction) {
        state.selectedTransaction?.let {
            if (!it.userID.isNullOrEmpty()) {
                events(TransactionEvents.OnGetUser(it.id ?: ""))
            }
            events(TransactionEvents.OnGetTransactionInbox(it.id ?: ""))
        }
    }
    LaunchedEffect(state) {
        if (state.errors != null) {
            context.toast(state.errors)
        }
    }
    Row(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .weight(0.5f)
        ) {
            item {
                Text(
                    "Transactions",
                    style = MaterialTheme.typography.titleLarge
                )
            }
            if (state.isLoading) {
                item {
                    LinearProgressIndicator(
                        modifier = modifier.fillMaxWidth()
                    )
                }
            }
            item {
                Spacer(modifier = modifier.height(8.dp))
            }
            item {
                SearchText(
                    label = "Search transaction here...",
                    value = state.searchText,
                    onChange = {events(TransactionEvents.OnSearch(it))}
                )
            }
            items(state.filteredTransactions) {
                TransactionItem(
                    transaction = it,
                    isSelected = state.selectedTransaction?.id == it.id,
                    onClick = {
                        events(TransactionEvents.OnSelectTranaction(it))
                    }
                )
            }
        }
        Box(
            modifier = modifier
                .fillMaxSize()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            if(state.selectedTransaction != null) {
                SelectedTransactionCard(
                    transaction = state.selectedTransaction,
                    userState = state.userState,
                    inboxState = state.inboxState
                )
            } else {
                Text("No Selected Transaction")
            }
        }
    }
}



