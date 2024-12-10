package com.eutech.pawprints.shared.domain.repository.transactions

import android.util.Log
import com.eutech.pawprints.appointments.data.appointment.AppointmentStatus
import com.eutech.pawprints.appointments.data.appointment.Inbox
import com.eutech.pawprints.appointments.data.appointment.InboxTpe
import com.eutech.pawprints.appointments.domain.INBOX_COLLECTION
import com.eutech.pawprints.products.domain.ProductRepository
import com.eutech.pawprints.shared.data.transactions.PaymentStatus
import com.eutech.pawprints.shared.data.transactions.Transaction
import com.eutech.pawprints.shared.data.transactions.TransactionStatus
import com.eutech.pawprints.shared.data.transactions.TransactionWithUser
import com.eutech.pawprints.shared.data.transactions.createMessage
import com.eutech.pawprints.shared.data.users.USERS_COLLECTION
import com.eutech.pawprints.shared.data.users.Users
import com.eutech.pawprints.shared.presentation.utils.Results
import com.eutech.pawprints.shared.presentation.utils.generateRandomNumber
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.Date

const val TRANSACTION_COLLECTION = "transactions"
class TransactionRepositoryImpl(
    private val firestore: FirebaseFirestore,
    private val productRepository: ProductRepository,
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

    override suspend fun getAllTransactions(result: (Results<List<Transaction>>) -> Unit) {
        result.invoke(Results.loading("Getting transactions"))
        delay(1000)
        firestore.collection(TRANSACTION_COLLECTION)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .orderBy("updatedAt", Query.Direction.DESCENDING)
            .addSnapshotListener { value, error ->
                Log.e(TRANSACTION_COLLECTION,"error",error)
                value?.let {
                    result(Results.success(it.toObjects(Transaction::class.java)))
                }
                error?.let {
                    result(Results.failuire(it.message.toString()))
                }
            }
    }

    override suspend fun getAllOnGoingTransaction(result: (Results<List<TransactionWithUser>>) -> Unit) {
        result.invoke(Results.loading("Getting transactions"))
        val stautus = listOf(AppointmentStatus.CANCELLED,AppointmentStatus.COMPLETED)
        delay(1000)
        firestore.collection(TRANSACTION_COLLECTION)
            .whereNotIn("status",stautus)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .orderBy("updatedAt", Query.Direction.DESCENDING)
            .addSnapshotListener { value, error ->
                Log.e(TRANSACTION_COLLECTION,"error",error)
                value?.let {
                    val transactions = it.toObjects(Transaction::class.java)
                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            val transactionWithUsers = transactions.map { transaction ->
                                val user = transaction.userID?.let { userId ->
                                    firestore.collection(USERS_COLLECTION)
                                        .document(userId)
                                        .get()
                                        .await()
                                        .toObject(Users::class.java)
                                }
                                TransactionWithUser(transaction, user)
                            }
                            withContext(Dispatchers.Main) {
                                result(Results.success(transactionWithUsers))
                            }
                        } catch (e: Exception) {
                            Log.e(TRANSACTION_COLLECTION, "Error fetching user data", e)
                            withContext(Dispatchers.Main) {
                                result(Results.failuire(e.message ?: "Error fetching user data"))
                            }
                        }
                    }
                }
                error?.let {
                    result(Results.failuire(it.message.toString()))
                }
            }
    }

    override suspend fun updateStatus(
        id: String,
        users: Users?,
        status: TransactionStatus,
        transaction: Transaction,
        result: (Results<String>) -> Unit
    ) {
        result.invoke(Results.loading("Updating transaction status..."))
        delay(1000)

        try {

            val batch = firestore.batch()
            if (status== TransactionStatus.COMPLETED) {
                productRepository.updateProductsQuantity(transaction.items)
            }
            val inbox = Inbox(
                id = generateRandomNumber(),
                userID = users?.id,
                collectionID = id,
                type = InboxTpe.TRANSACTIONS,
                message = status.createMessage()
            )
            val inboxRef = firestore.collection(INBOX_COLLECTION).document(inbox.id!!)
            batch.set(inboxRef, inbox)
            val transactionRef = firestore.collection(TRANSACTION_COLLECTION).document(id)
            batch.update(transactionRef, mapOf(
                "status" to status,
                "updatedAt" to Date()
            ))
            batch.commit().await()
            result.invoke(Results.success("Transaction status updated successfully."))
        } catch (e: Exception) {
            Log.e("Firestore", "Error updating transaction status", e)
            result.invoke(Results.failuire(e.message ?: "Error updating transaction status."))
        }
    }

    override suspend fun addPayment(
        id: String,
        users: Users?,
        amountReceived: Double,
        result: (Results<String>) -> Unit
    ) {
        result.invoke(Results.loading("Updating Payment..."))
        delay(1000)

        try {
            val batch = firestore.batch()
            val inbox = Inbox(
                id = generateRandomNumber(),
                userID = users?.id,
                collectionID = id,
                type = InboxTpe.PAYMENT,
                message = "Order paid"
            )
            val inboxRef = firestore.collection(INBOX_COLLECTION).document(inbox.id!!)
            batch.set(inboxRef, inbox)

            val transactionRef = firestore.collection(TRANSACTION_COLLECTION).document(id)
            batch.update(transactionRef, mapOf(
                "payment.status" to PaymentStatus.PAID,
                "updatedAt" to Date()
            ))
            batch.commit().await()
            result.invoke(Results.success("Payment updated"))
        } catch (e: Exception) {
            Log.e("Firestore", "Error updating transaction status", e)
            result.invoke(Results.failuire(e.message ?: "Error updating transaction status."))
        }
    }

    override suspend fun createTransaction(
        transaction: Transaction,
        result: (Results<String>) -> Unit
    ) {
        val batch = firestore.batch()

        productRepository.updateProductsQuantity(transaction.items)
        val inbox = Inbox(
            id = generateRandomNumber(),
            userID = transaction.id,
            collectionID = transaction.id,
            type = InboxTpe.PAYMENT,
            message = "Order paid"
        )

        val inboxRef = firestore.collection(INBOX_COLLECTION).document(inbox.id!!)
        batch.set(inboxRef, inbox)

        val transactionRef = firestore.collection(TRANSACTION_COLLECTION).document(transaction.id!!)
        batch.set(transactionRef, transaction)

        try {

            batch.commit().await()
            result(Results.success("Transaction created successfully"))
        } catch (e: Exception) {

            result(Results.failuire(e.localizedMessage ?: "An error occurred while creating the transaction"))
        }
    }


}