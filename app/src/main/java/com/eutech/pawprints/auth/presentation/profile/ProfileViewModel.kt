package com.eutech.pawprints.auth.presentation.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eutech.pawprints.auth.domain.AuthRepository
import com.eutech.pawprints.shared.presentation.utils.Results
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel

class ProfileViewModel @Inject constructor(
     private val authRepository: AuthRepository
) : ViewModel() {
    var state by mutableStateOf(ProfileState())
    init {
        events(ProfileEvents.OnGetAdminInfo)
    }
    fun events(e : ProfileEvents) {
        when(e) {
            ProfileEvents.OnGetAdminInfo -> getAdminInfo()
        }

    }

    private fun getAdminInfo() {
        viewModelScope.launch {
            authRepository.getAdminData {
                state = when(it) {
                    is Results.failuire -> state.copy(
                        isLoading = false,
                        errors = it.message,
                    )
                    is Results.loading -> state.copy(
                        isLoading = true,
                        errors = null
                    )
                    is Results.success -> state.copy(
                        isLoading = false,
                        errors = null,
                        administrator = it.data
                    )
                }
            }
        }
    }
}