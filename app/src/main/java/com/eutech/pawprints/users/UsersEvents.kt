package com.eutech.pawprints.users

import com.eutech.pawprints.shared.data.users.Users


sealed interface UsersEvents {
    data object  OnGetAllUsers : UsersEvents
    data class OnSelectUser(val users: Users ?) : UsersEvents
    data class OnSearch(val text : String) : UsersEvents
    data class OnGetPets(val userID : String) : UsersEvents
    data class OnGetInbox(val userID : String) : UsersEvents
    data class OnGetAppointments(val userID : String) : UsersEvents
    data class OnGetTransactions(val userID : String) : UsersEvents
}