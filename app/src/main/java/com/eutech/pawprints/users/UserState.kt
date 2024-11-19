package com.eutech.pawprints.users

import com.eutech.pawprints.shared.data.users.Users


data class UserState(
    val isLoading : Boolean = false,
    val users : List<Users> = emptyList(),
    val errors : String ? = null,
    val selectedUser : Users ? = null,
    val filteredUser : List<Users> = emptyList(),
    val searchText : String = ""
)