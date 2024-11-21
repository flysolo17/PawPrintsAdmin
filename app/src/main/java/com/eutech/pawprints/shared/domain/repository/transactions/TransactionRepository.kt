package com.eutech.pawprints.shared.domain.repository.transactions


import com.eutech.pawprints.shared.data.transactions.Transaction
import com.eutech.pawprints.shared.presentation.utils.Results


interface TransactionRepository {
    suspend fun getMyTransactions(
        userID : String,
        result : (Results<List<Transaction>>) -> Unit
    )
}