package com.eutech.pawprints.shared.presentation

import com.eutech.pawprints.auth.data.Administrator

data class AuthState(
    val isLoading : Boolean = false,
    val admin : Administrator ? = null,
    val errors : String ? = null,

)
