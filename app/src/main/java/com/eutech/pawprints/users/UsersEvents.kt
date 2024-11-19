package com.eutech.pawprints.users

import com.eutech.pawprints.shared.data.users.Users


sealed interface UsersEvents {
    data object  OnGetAllUsers : UsersEvents
    data class OnSelectUser(val users: Users ?) : UsersEvents
    data class OnSearch(val text : String) : UsersEvents
}