package com.eutech.pawprints.shared.domain.repository.transactions

import com.eutech.pawprints.appointments.data.appointment.Inbox
import com.eutech.pawprints.shared.data.transactions.Transaction
import com.eutech.pawprints.shared.presentation.utils.Results
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.delay

const val TRANSACTION_COLLECTION = "transactions"
class TransactionRepositoryImpl(
    private val firestore: FirebaseFirestore
): TransactionRepository {
    override suspend fun getMyTransactions(
        userID: String,
        result: (Results<List<Transaction>>) -> Unit
    ) {
        result.invoke(Results.loading("Getting transactions"))
        delay(1000)
        firestore.collection(TRANSACTION_COLLECTION)
            .whereEqualTo("userID",userID)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .orderBy("updatedAt", Query.Direction.DESCENDING)
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    result(Results.success(it.result.toObjects(Transaction::class.java)))
                } else {
                    result(Results.failuire("Error getting transactions"))
                }
            }.addOnFailureListener {
                result(Results.failuire(it.message.toString()))
            }
    }
}