package com.eutech.pawprints.transactions


import com.eutech.pawprints.appointments.data.appointment.Inbox
import com.eutech.pawprints.shared.data.transactions.Transaction
import com.eutech.pawprints.shared.data.users.Users

data class TransactionsState(
    val isLoading : Boolean = false,
    val filteredTransactions : List<Transaction> = emptyList(),
    val transactions : List<Transaction> = emptyList(),
    val errors : String? = null,

    val selectedTransaction : Transaction ? = null,
    val searchText : String = "",

    val userState: UserState = UserState(),
    val inboxState: InboxState = InboxState(),
)

data class UserState(
    val isLoading : Boolean = false,
    val users : Users? = null,
    val errors : String ? = null,
)

data class InboxState(
    val isLoading : Boolean = false,
    val inboxes : List<Inbox> = emptyList(),
    val errors : String ? = null,
)