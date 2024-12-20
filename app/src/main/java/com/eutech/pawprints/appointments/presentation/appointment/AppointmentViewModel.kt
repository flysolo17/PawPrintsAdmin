package com.eutech.pawprints.appointments.presentation.appointment

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eutech.pawprints.appointments.data.appointment.AppointmentStatus
import com.eutech.pawprints.appointments.data.appointment.Appointments
import com.eutech.pawprints.appointments.domain.AppointmentRepository
import com.eutech.pawprints.shared.presentation.utils.Results
import com.eutech.pawprints.shared.presentation.utils.toast
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import javax.inject.Inject


@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class AppointmentViewModel @Inject constructor(
     private val appointmentRepository: AppointmentRepository
) : ViewModel() {
    var state by mutableStateOf(AppointmentState())

    init {
        events(AppointmentEvent.OnGetAllAppointment)
    }

    fun events(e : AppointmentEvent) {
        when(e) {
            AppointmentEvent.OnGetAllAppointment -> getAppointments()
            is AppointmentEvent.OnTabSelected -> state = state.copy(
                selectedTab = e.tab,

            )
            is AppointmentEvent.OnUpdateAppointment -> updateStatus(
                e.status,
                e.appointments
            )
        }
    }
    private fun updateStatus(status: AppointmentStatus, appointments: Appointments) {
        viewModelScope.launch {
            appointmentRepository.updateAppointmentStatus(
                status = status,
                appointments = appointments
            ) {
                state = when (it) {
                    is Results.failuire -> state.copy(
                        updatingStatus = false,
                        errors = it.message,
                    )
                    is Results.loading -> state.copy(
                        updatingStatus = true,
                        errors = null,
                    )
                    is Results.success -> state.copy(
                        updatingStatus = false,
                        errors = null,
                        isUpdated = it.data
                    )
                }

            }
            delay(1000)
            state = state.copy(
                updatingStatus = false,
                errors = null,
                isUpdated = null
            )
        }


    }

//    private fun updateStatus(id : String, status: AppointmentStatus, context: Context) {
//        viewModelScope.launch {
//            appointmentRepository.updateAppointmentStatus(id,status) {
//                if (it is Results.success) {
//                    context.toast(it.data)
//                }
//                if (it is Results.failuire) {
//                    context.toast(it.message)
//                }
//            }
//        }
//    }

    private fun getAppointments() {
       viewModelScope.launch {
           appointmentRepository.getAppointmentWithAttendeesAndPets {
               state = when(it) {
                   is Results.failuire -> state.copy(
                       isLoading = false,
                       errors = it.message
                   )
                   is Results.loading ->state.copy(
                       isLoading = true,
                       errors = null
                   )
                   is Results.success -> state.copy(
                       isLoading = false,
                       errors = null,
                       appointments = it.data
                   )
               }
           }
       }
    }
}