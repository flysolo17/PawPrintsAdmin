package com.eutech.pawprints.orders

import com.eutech.pawprints.shared.data.transactions.Payment
import com.eutech.pawprints.shared.data.transactions.Transaction
import com.eutech.pawprints.shared.data.transactions.TransactionStatus
import com.eutech.pawprints.shared.data.transactions.TransactionWithUser
import com.eutech.pawprints.shared.data.users.Users


sealed interface OrderEvents  {
    data object OnGetTransactions : OrderEvents
    data class OnSearching(val text : String) : OrderEvents
    data class OnSelectTransaction(val transactionWithUser: TransactionWithUser?) : OrderEvents
    data class OnAmountReceivedChange(
        val text  : String
    ) : OrderEvents
    data class OnSelectTransactionForPayment(val transactionWithUser: TransactionWithUser?) : OrderEvents

    data class OnUpdateTransactionStatus(
        val id : String,
        val user : Users ?,
        val status : TransactionStatus,
        val transaction : Transaction
    ) : OrderEvents

    data class OnAddPayment(
        val id : String,
        val user : Users ?,
        val amountReceived : Double
    ) : OrderEvents
}