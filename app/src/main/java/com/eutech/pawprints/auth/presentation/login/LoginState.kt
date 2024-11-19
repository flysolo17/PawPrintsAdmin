package com.eutech.pawprints.auth.presentation.login

import com.eutech.pawprints.auth.data.Administrator
import com.eutech.pawprints.shared.presentation.utils.TextFieldData

data class LoginState(
    val email : TextFieldData = TextFieldData(),
    val password : TextFieldData = TextFieldData(),
    val isLoading : Boolean = false,
    val errors : String ? = null,
    val isPasswordVisible : Boolean = false,
    val admin : Administrator ? = null,
)


