package com.eutech.pawprints.transactions

import com.eutech.pawprints.shared.data.transactions.Transaction


sealed interface TransactionEvents {
    data object OnGetAllTransactions : TransactionEvents
    data class OnSearch(val text : String) : TransactionEvents
    data class OnSelectTranaction(
        val  transaction: Transaction
    ) : TransactionEvents

    data class OnGetUser(
        val userID : String
    ) : TransactionEvents
    data class OnGetTransactionInbox(
        val transactionID : String
    ) : TransactionEvents
}