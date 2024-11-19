package com.eutech.pawprints.doctors.presentation.doctor

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eutech.pawprints.doctors.data.Doctors
import com.eutech.pawprints.doctors.domain.DoctorRepository
import com.eutech.pawprints.shared.presentation.utils.Results
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel

class DoctorViewModel @Inject constructor(
     private  val doctorRepository: DoctorRepository
) : ViewModel() {
    var state by mutableStateOf(DoctorState())
    init {
        events(DoctorEvents.OnGetAllDoctors)
    }
    fun events(e : DoctorEvents) {
        when(e) {
            DoctorEvents.OnGetAllDoctors -> getAllDoctors()
            is DoctorEvents.OnDeleteDoctor -> deleteDoctor(e.doctors)
        }
    }

    private fun deleteDoctor(doctors: Doctors) {
        viewModelScope.launch {
            doctorRepository.deleteDoctor(doctors) {
                state = when(it) {
                    is Results.failuire -> state.copy(
                        isLoading = false,
                        errors = it.message
                    )
                    is Results.loading -> state.copy(
                        isLoading = true
                    )
                    is Results.success ->state.copy(
                        isLoading = false,
                        errors = null,
                        deleteSuccess = it.data,
                    )
                }
            }
        }
    }

    private fun getAllDoctors() {
        viewModelScope.launch {
            doctorRepository.getAllDoctors {
                state = when(it) {
                    is Results.failuire -> state.copy(
                        isLoading = false,
                        errors = it.message
                    )
                    is Results.loading -> state.copy(
                        isLoading = true,
                        errors = null
                    )
                    is Results.success -> state.copy(
                        isLoading = false,
                        doctors = it.data
                    )
                }
            }
        }
    }
}