package com.eutech.pawprints.shared.data.transactions

import android.os.Parcelable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Cancel
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.DirectionsCar
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material.icons.rounded.HelpOutline
import androidx.compose.material.icons.rounded.HourglassEmpty
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.eutech.pawprints.appointments.data.appointment.AppointmentStatus
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


fun TransactionStatus?.getColor(): Color {
    return when (this) {
        TransactionStatus.ACCEPTED -> Color(0xFF4CAF50)  // green
        TransactionStatus.PENDING -> Color(0xFFFFEB3B)    // yellow
        TransactionStatus.CANCELLED -> Color(0xFFF44336)  // red
        TransactionStatus.COMPLETED -> Color(0xFF2E7D32)  // dark green
        TransactionStatus.TO_PICK_UP ->  Color(0xFFFFEB3B)
        else -> Color(0xFFF44336)
    }
}

fun List<TransactionItems>.computeTotal(): Double {
    return this.sumOf { it.quantity!! * it.price!! }
}


fun TransactionStatus?.getIcon(): ImageVector {
    return when (this) {
        TransactionStatus.ACCEPTED -> Icons.Rounded.CheckCircle
        TransactionStatus.PENDING -> Icons.Rounded.HourglassEmpty
        TransactionStatus.CANCELLED -> Icons.Rounded.Cancel
        TransactionStatus.COMPLETED -> Icons.Rounded.Done
        TransactionStatus.TO_PICK_UP -> Icons.Rounded.DirectionsCar
        else -> Icons.Rounded.HelpOutline
    }
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
    PICK_UP,
    IN_STORE,
}