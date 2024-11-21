package com.eutech.pawprints.shared.data.transactions

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Date


@Parcelize
data class Transaction(
    val id : String ?  = null,
    var userID : String ? = null,
    val items: List<TransactionItems> = emptyList(),
    val type : TransactionType = TransactionType.PICK_UP,
    val status : TransactionStatus = TransactionStatus.PENDING,
    val payment: Payment ?= Payment(),
    val createdAt : Date = Date(),
    val updatedAt : Date = Date(),
) : Parcelable
enum class TransactionStatus(val status: String) {
    PENDING("PENDING"),
    ACCEPTED("ACCEPTED"),
    TO_PICK_UP("TO_PICK_UP"),
    COMPLETED("COMPLETED"),
    CANCELLED("CANCELLED")
}


fun TransactionStatus.createMessage(): String {
    return when (this) {
        TransactionStatus.PENDING -> "Your transaction is currently pending. Please wait for further updates."
        TransactionStatus.ACCEPTED -> "Your transaction has been accepted and is being processed."
        TransactionStatus.CANCELLED -> "Your transaction has been cancelled. Let us know if you need help."
        TransactionStatus.COMPLETED -> "Your transaction has been successfully completed. Thank you!"
        TransactionStatus.TO_PICK_UP -> "Your Order is ready to pick up"
    }
}

enum class TransactionType {
    PICK_UP
}