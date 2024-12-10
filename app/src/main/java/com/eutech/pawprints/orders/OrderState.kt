package com.eutech.pawprints.orders

import androidx.compose.ui.graphics.drawscope.Stroke
import com.eutech.pawprints.shared.data.transactions.Transaction
import com.eutech.pawprints.shared.data.transactions.TransactionWithUser


data class OrderState(
    val isLoading : Boolean = false,
    val filteredTransaction : List<TransactionWithUser> = emptyList(),
    val transactions : List<TransactionWithUser> = emptyList(),
    val errors : String? = null,

    val searchText : String = "",
    val selectedTransaction : TransactionWithUser ? = null,

    val selectedTransactionForPayment : TransactionWithUser ? = null,
    val amountReceived : String = "0.00",


    //update
    val isUpdating : Boolean = false,
    val isUpdated : String ? = null,

    //payment
    val isPaying : Boolean = false,
    val isPaymentUpdated : String ? = null,
)

