package com.eutech.pawprints.appointments.data.appointment

import java.util.Date


data class Inbox(
    val id : String ? = null,
    val userID : String ? = null,
    val collectionID : String ? = null,
    val type : InboxTpe = InboxTpe.APPOINTMENTS,
    val message : String ? = "",
    val seen : Boolean =false,
    val createdAt : Date = Date()
)

enum class InboxTpe {
    APPOINTMENTS,
    TRANSACTIONS,
    PAYMENT
}

