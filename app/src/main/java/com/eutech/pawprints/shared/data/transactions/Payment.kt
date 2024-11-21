package com.eutech.pawprints.shared.data.transactions

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Date


@Parcelize
data class Payment(
    val id : String ? = null,
    val total : Double ? = 0.00,
    val type : PaymentType = PaymentType.CASH,
    val status : PaymentStatus = PaymentStatus.PENDING,
    val reference : String ? = null,
    val createdAt : Date = Date(),
    val updatedAt : Date = Date()
) : Parcelable

enum class PaymentType {
    CASH
}
enum class PaymentStatus(val status: String)  {
    PENDING("PENDING"),
    PAID("PAID"),
    UNPAID("UNPAID"),
}