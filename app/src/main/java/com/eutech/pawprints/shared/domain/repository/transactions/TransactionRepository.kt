package com.eutech.pawprints.shared.domain.repository.transactions


import com.eutech.pawprints.shared.data.transactions.Transaction
import com.eutech.pawprints.shared.data.transactions.TransactionStatus
import com.eutech.pawprints.shared.data.transactions.TransactionWithUser
import com.eutech.pawprints.shared.data.users.Users
import com.eutech.pawprints.shared.presentation.utils.Results


interface TransactionRepository {
    suspend fun getMyTransactions(
        userID : String,
        result : (Results<List<Transaction>>) -> Unit
    )

    suspend fun getAllTransactions(
        result: (Results<List<Transaction>>) -> Unit
    )
    suspend fun getAllOnGoingTransaction(
        result: (Results<List<TransactionWithUser>>) -> Unit
    )


    suspend fun updateStatus(
        id : String,
        users: Users?,
        status : TransactionStatus,
        transaction: Transaction,
        result: (Results<String>) -> Unit
    )

    suspend fun addPayment(
        id : String,
        users: Users?,
        amountReceived : Double ,
        result: (Results<String>) -> Unit
    )

    suspend fun createTransaction(
        transaction: Transaction,
        result: (Results<String>) -> Unit
    )
}