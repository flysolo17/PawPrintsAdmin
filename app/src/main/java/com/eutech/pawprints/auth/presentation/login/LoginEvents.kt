package com.eutech.pawprints.auth.presentation.login

import android.content.Context
import android.net.Uri
import android.util.Log

sealed interface LoginEvents {
    data class OnEmailChanged(val email: String) : LoginEvents
    data class OnPasswordChange(val password: String) : LoginEvents
    data object OnGetAdminInfo : LoginEvents
    data class OnLogin(
        val email : String,
        val password : String
    ) : LoginEvents
    data object OnTogglePassword : LoginEvents
}