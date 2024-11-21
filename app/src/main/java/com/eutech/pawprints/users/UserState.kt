package com.eutech.pawprints.users

import com.eutech.pawprints.appointments.data.appointment.Appointments
import com.eutech.pawprints.appointments.data.appointment.Inbox
import com.eutech.pawprints.shared.data.pets.Pet
import com.eutech.pawprints.shared.data.transactions.Transaction
import com.eutech.pawprints.shared.data.users.Users


data class UserState(
    val isLoading : Boolean = false,
    val users : List<Users> = emptyList(),
    val errors : String ? = null,
    val selectedUser : Users ? = null,
    val filteredUser : List<Users> = emptyList(),
    val searchText : String = "",
    val petTabState: PetTabState = PetTabState(),
    val inboxTabState : InboxTabState = InboxTabState(),
    val appointmentTabState: AppointmentTabState = AppointmentTabState(),
    val transactionTabState : TransactionTabState = TransactionTabState()
)

data class PetTabState(
    val isLoading: Boolean = false,
    val pets : List<Pet> = emptyList(),
    val errors : String ? = null,
)
data class InboxTabState(
    val isLoading: Boolean = false,
    val inboxes : List<Inbox> = emptyList(),
    val errors : String ? = null,
)

data class AppointmentTabState(
    val isLoading: Boolean = false,
    val appointments : List<Appointments> = emptyList(),
    val errors : String ? = null,
)

data class TransactionTabState(
    val isLoading: Boolean = false,
    val transactions : List<Transaction> = emptyList(),
    val errors : String ? = null,
)