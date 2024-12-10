package com.eutech.pawprints.orders

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.eutech.pawprints.orders.components.OrderHeader
import com.eutech.pawprints.orders.components.OrderInfoDialog
import com.eutech.pawprints.orders.components.OrderItems
import com.eutech.pawprints.shared.presentation.components.PaymentDialog
import com.eutech.pawprints.shared.presentation.utils.toast


@Composable
fun OrderScreen(
    modifier: Modifier = Modifier,
    state: OrderState,
    events: (OrderEvents) -> Unit,
    navHostController: NavHostController
) {
    val context = LocalContext.current
    LaunchedEffect(state) {
        if (state.errors != null) {
            context.toast(state.errors)
        }
    }
    if (state.selectedTransaction != null) {
        OrderInfoDialog(
            transactionWithUser = state.selectedTransaction,
            onDismiss = { events(OrderEvents.OnSelectTransaction(null)) },
            onConfirm = { status ->
                val selected = state.selectedTransaction
                val transaction = selected.transaction
                events(OrderEvents.OnUpdateTransactionStatus(selected.transaction.id!!,selected.users,status,transaction))
                events(OrderEvents.OnSelectTransaction(null))
            }
        )
    }
    if (state.selectedTransactionForPayment != null) {
        val transaction = state.selectedTransactionForPayment.transaction
        val user = state.selectedTransactionForPayment.users
        PaymentDialog(
            amount = transaction.payment?.total ?: 0.00,
            amountReceived = state.amountReceived,
            isPaying = state.isPaying,
            onChange = {events(OrderEvents.OnAmountReceivedChange(it))},
            onConfirm = {
                events(
                    OrderEvents.OnAddPayment(
                        id = transaction.id!!,
                        user = user,
                        amountReceived = it
                    )
                )
            },
            onDismiss = {
                events(OrderEvents.OnSelectTransactionForPayment(null))
            }
        )
    }
    LazyColumn {
        item {
            OrderHeader(
                onBackPress = {navHostController.popBackStack()},
                searchText = state.searchText,
                onSearching = {events(OrderEvents.OnSearching(it))}
            )
        }
        if (state.isLoading) {
            item {
                LinearProgressIndicator(
                    modifier = modifier.fillMaxWidth()
                )
            }
        }
        items(state.filteredTransaction){
            OrderItems(
                transactionWithUser = it,
                isUpdating = state.isUpdating,
                onEdit = {events(OrderEvents.OnSelectTransaction(it))},
                onAddPayment = {events(OrderEvents.OnSelectTransactionForPayment(it))}
            )
        }

    }
}



