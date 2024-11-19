package com.eutech.pawprints.doctors.presentation.create_doctor

import android.graphics.Color
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eutech.pawprints.doctors.data.Doctors
import com.eutech.pawprints.doctors.domain.DoctorRepository
import com.eutech.pawprints.doctors.presentation.doctor.DoctorEvents
import com.eutech.pawprints.doctors.presentation.doctor.DoctorState
import com.eutech.pawprints.shared.presentation.utils.Results
import com.eutech.pawprints.shared.presentation.utils.generateRandownString
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel

class CreateDoctorViewModel @Inject constructor(
     private val doctorRepository: DoctorRepository
) : ViewModel() {
    var state by mutableStateOf(CreateDoctorState())

    fun events(e : CreateDoctorEvents) {
        when(e ) {
            is CreateDoctorEvents.OnEmailChanged -> emailChange(e.email)
            is CreateDoctorEvents.OnNameChanged -> nameChange(e.name)
            is CreateDoctorEvents.OnPhoneChanged -> phoneChange(e.phone)
            is CreateDoctorEvents.OnProfileChanged -> profileChange(e.uri)
            CreateDoctorEvents.OnCreateDoctor -> createDoctor()
            is CreateDoctorEvents.OnColorSelected -> selectColor(e.color)
        }
    }

    private fun selectColor(color: Int) {
        state = state.copy(tag = color)
    }

    private fun createDoctor() {
        viewModelScope.launch {
            val doctor = Doctors(
                id = generateRandownString(20),
                name = state.name.value,
                phone = state.phone.value,
                email = state.email.value,
                tag = state.tag
            )
            doctorRepository.createDoctor(doctors = doctor, uri = state.profile) {
                state = when(it) {
                    is Results.failuire ->
                        state.copy(
                            isLoading = false,
                            errors = it.message
                        )
                    is Results.loading -> state.copy(
                        isLoading = true,
                        errors = null
                    )
                    is Results.success -> state.copy(
                        isLoading = false,
                        created = it.data
                    )
                }
            }
        }
    }

    private fun profileChange(uri: Uri?) {
        state = state.copy(
            profile = uri
        )
    }

    private fun phoneChange(phone: String) {
        var errorMessage: String? = null
        var hasError = false

        if (phone.isEmpty()) {
            errorMessage = "Phone number cannot be empty"
            hasError = true
        } else if (!phone.startsWith("09")) {
            errorMessage = "Phone number must start with 09"
            hasError = true
        } else if (phone.length != 11) {
            errorMessage = "Phone number must be 11 characters long"
            hasError = true
        } else if (!phone.all { it.isDigit() }) {
            errorMessage = "Phone number can only contain digits"
            hasError = true
        }

        state = state.copy(
            phone = state.phone.copy(
                value = phone,
                errorMessage = errorMessage,
                hasError = hasError
            )
        )
    }

    private fun nameChange(name: String) {
        var errorMessage: String? = null
        var hasError = false

        if (name.isEmpty()) {
            errorMessage = "Name cannot be empty"
            hasError = true
        } else if (!name.all { it.isLetter() || it.isWhitespace() }) {
            errorMessage = "Name can only contain letters and spaces"
            hasError = true
        }

        state = state.copy(
            name = state.name.copy(
                value = name,
                errorMessage = errorMessage,
                hasError = hasError
            )
        )
    }

    private fun emailChange(email: String) {
        var errorMessage: String? = null
        var hasError = false

        if (email.isEmpty()) {
            errorMessage = "Email cannot be empty"
            hasError = true
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            errorMessage = "Invalid email address"
            hasError = true
        }

        state = state.copy(
            email = state.email.copy(
                value = email,
                errorMessage = errorMessage,
                hasError = hasError
            )
        )
    }
}