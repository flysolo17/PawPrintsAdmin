package com.eutech.pawprints.home.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eutech.pawprints.appointments.data.appointment.AppointmentStatus
import com.eutech.pawprints.appointments.data.appointment.AppointmentWithAttendeesAndPets
import com.eutech.pawprints.appointments.data.appointment.Appointments
import com.eutech.pawprints.appointments.domain.AppointmentRepository
import com.eutech.pawprints.auth.domain.AuthRepository
import com.eutech.pawprints.home.data.CartItems
import com.eutech.pawprints.home.data.ProductWithCart
import com.eutech.pawprints.products.data.Category
import com.eutech.pawprints.products.data.products.ProductType
import com.eutech.pawprints.products.data.products.Products
import com.eutech.pawprints.products.domain.CategoryRepository
import com.eutech.pawprints.products.domain.ProductRepository
import com.eutech.pawprints.schedule.domain.ScheduleRepository
import com.eutech.pawprints.schedule.presentation.ScheduleEvents
import com.eutech.pawprints.shared.presentation.utils.Results
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate
import javax.inject.Inject


@HiltViewModel

class HomeViewModel @Inject constructor(
     private val authRepository: AuthRepository,
    private val scheduleRepository: ScheduleRepository,
    private val appointmentRepository: AppointmentRepository,
) : ViewModel() {
    var state by mutableStateOf(HomeState())

    init {
        events(HomeEvents.OnGetSchedules(state.selectedDate))
    }
    fun events(e : HomeEvents) {
        when(e) {
            is HomeEvents.OnGetSchedules -> getSchedule(e.localDate)
            is HomeEvents.OnDateChange -> state = state.copy(selectedDate = e.localDate)
            is HomeEvents.OnGetAppointments -> getAppointment(e.localDate)
           is HomeEvents.OnAutoCancelAppointments -> autoCancel(e.appointments)
            is HomeEvents.OnUpdateAppointment -> updateStatus(
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

    private fun autoCancel(appointments: List<AppointmentWithAttendeesAndPets>) {

        viewModelScope.launch {
            appointmentRepository.cancelAppointmentDueToPastDate(appointments) {
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
                        errors = null,
                        messages = it.data
                    )
                }
            }
        }
    }

    private fun getAppointment(localDate: LocalDate) {
        viewModelScope.launch {
            appointmentRepository.getAppointmentWithAttendeesAndPets(localDate) {
                state = when(it) {
                    is Results.failuire -> state.copy(
                        isGettingAppointments = false,
                        errors = it.message,
                    )
                    is Results.loading -> state.copy(
                        isGettingAppointments = true,
                        errors = null,
                    )
                    is Results.success -> state.copy(
                        isGettingAppointments = false,
                        errors = null,
                        appointment = it.data
                    )
                }
            }
        }
    }

    private fun getSchedule(localDate: LocalDate) {
        state = state.copy(schedules = emptyList())
        viewModelScope.launch {
            scheduleRepository.getScheduleByMonth(localDate) {
                state = when(it) {
                    is Results.failuire -> state.copy(
                        isLoading = false,
                        errors = it.message
                    )
                    is Results.loading -> state.copy(
                        isLoading =true,
                        errors = null
                    )
                    is Results.success -> state.copy(
                        isLoading = false,
                        errors = null,
                        schedules = it.data
                    )
                }
            }
        }
        events(HomeEvents.OnGetAppointments(localDate))
    }
}