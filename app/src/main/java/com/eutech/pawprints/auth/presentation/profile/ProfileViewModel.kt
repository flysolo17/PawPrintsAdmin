package com.eutech.pawprints.auth.presentation.profile

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eutech.pawprints.auth.domain.AuthRepository
import com.eutech.pawprints.shared.presentation.utils.Results
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
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
            is ProfileEvents.OnProfileChange -> uploadProfile(e.uri)
            is ProfileEvents.OnUpdate -> editProfile(e.id,e.name,e.phone,e.email)
        }

    }

    private fun editProfile(id: String, name: String, phone: String, email: String) {
        viewModelScope.launch {
            authRepository.editProfile(id,name,phone,email) {
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
                        isChanged = it.data
                    )
                }
            }
            delay(1000)
            state =state.copy(
                isChanged = null
            )
        }
    }

    private fun uploadProfile(uri: Uri) {
        viewModelScope.launch {
            authRepository.updateProfile(state.administrator?.id ?: "",uri) {
                state = when(it) {
                    is Results.failuire -> state.copy(
                        isChangingProfile = false,
                        errors = it.message,
                    )
                    is Results.loading -> state.copy(
                        isChangingProfile = true,
                        errors = null
                    )
                    is Results.success -> state.copy(
                        isChangingProfile = false,
                        errors = null,
                        isChanged = it.data
                    )
                }
            }
            delay(1000)
            state =state.copy(
                isChanged = null
            )
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