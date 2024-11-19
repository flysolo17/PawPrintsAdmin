package com.eutech.pawprints.shared.data.users


const val USERS_COLLECTION = "users"
data class Users(
    var id: String? = null,
    val name: String? = null,
    val phone: String? = null,
    val email: String? = null,
    val profile: String? = null
)