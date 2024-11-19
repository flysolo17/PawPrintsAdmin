package com.eutech.pawprints.auth.presentation.login

import android.content.Context
import android.net.Uri
import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eutech.pawprints.auth.domain.AuthRepository
import com.eutech.pawprints.shared.presentation.utils.Results
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel

class LoginViewModel @Inject constructor(
     private val authRepository: AuthRepository
) : ViewModel() {
    var state by mutableStateOf(LoginState())
    init {
        events(LoginEvents.OnGetAdminInfo)
    }
    fun events(e : LoginEvents) {
        when(e) {
            is LoginEvents.OnLogin -> login(e.email,e.password)
            is LoginEvents.OnEmailChanged -> emailChange(e.email)
            is LoginEvents.OnPasswordChange -> passwordChange(e.password)
            LoginEvents.OnGetAdminInfo -> getAdmin()
            LoginEvents.OnTogglePassword -> state = state.copy(isPasswordVisible = !state.isPasswordVisible)
        }
    }

    private fun getAdmin() {
        authRepository.getAdminData {
            state = when(it) {
                is Results.failuire -> state.copy(
                    isLoading = false
                )
                is Results.loading -> state.copy(
                    isLoading = true
                )
                is Results.success -> state.copy(
                    admin = it.data
                )
            }
        }
    }


    private fun passwordChange(password: String) {
        val newPassword = state.password.copy(
            value = password,
        )
        state = state.copy(password = newPassword)
    }

    private fun emailChange(email: String) {
        val hasError =  !Patterns.EMAIL_ADDRESS.matcher(email).matches() || email.contains(" ")
        val errorMessage = if (hasError)  {
            "Invalid email"
        } else {
            null
        }
        val newEmail = state.email.copy(
            value = email,
            hasError = hasError,
            errorMessage = errorMessage
        )
        state = state.copy(email = newEmail)
    }

    private fun login(email: String, password: String) {
        viewModelScope.launch {
            authRepository.login(email,password) {
                when(it) {
                    is Results.failuire -> {
                        state = state.copy(
                            isLoading = false,
                            errors = it.message
                        )
                    }
                    is Results.loading -> {
                        state = state.copy(
                            isLoading = true,
                            errors = null
                        )
                    }
                    is Results.success -> {
                        state = state.copy(
                            isLoading = false,
                            errors = null
                        )
                        events(LoginEvents.OnGetAdminInfo)
                    }
                }
            }

        }
    }
}