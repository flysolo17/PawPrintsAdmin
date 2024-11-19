package com.eutech.pawprints.appointments.presentation.create_appointment

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eutech.pawprints.appointments.data.appointment.AppointmentStatus
import com.eutech.pawprints.appointments.data.appointment.Appointments
import com.eutech.pawprints.appointments.data.appointment.AttendeeType
import com.eutech.pawprints.appointments.data.appointment.Attendees

import com.eutech.pawprints.appointments.domain.AppointmentRepository
import com.eutech.pawprints.doctors.data.Doctors
import com.eutech.pawprints.schedule.data.Hours
import com.eutech.pawprints.schedule.data.Meridiem
import com.eutech.pawprints.doctors.domain.DoctorRepository
import com.eutech.pawprints.products.data.products.ProductType
import com.eutech.pawprints.products.data.products.Products
import com.eutech.pawprints.products.domain.ProductRepository
import com.eutech.pawprints.shared.presentation.utils.Results
import com.eutech.pawprints.shared.presentation.utils.generateRandownString
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject


@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel

class CreateAppointmentViewModel @Inject constructor(
     private val doctorRepository: DoctorRepository,
    private val appointmentRepository: AppointmentRepository,
    private val productRepository: ProductRepository
) : ViewModel() {
    var state by mutableStateOf(CreateAppointmentState())
    var currentDate = Calendar.getInstance()
    init {
        val initialHour = currentDate.get(Calendar.HOUR_OF_DAY)
        val initialMinute = currentDate.get(Calendar.MINUTE)
        events(CreateAppointmentEvents.OnStartTimeChange(initialHour,initialMinute))
        events(CreateAppointmentEvents.OnEndTimeChange(initialHour,initialMinute))
        events(CreateAppointmentEvents.OnGetDoctors)
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun events(e  : CreateAppointmentEvents) {
        when(e) {
            CreateAppointmentEvents.OnGetDoctors -> getDoctors()
            is CreateAppointmentEvents.OnDateChange -> state = state.copy(
                date = e.date
            )

            is CreateAppointmentEvents.OnTitleChange -> state = state.copy(
                title = e.title
            )

            is CreateAppointmentEvents.OnEndTimeChange -> endTimeChange(e.hour,e.minute)
            is CreateAppointmentEvents.OnStartTimeChange -> startTimeChange(e.hour,e.minute)
            is CreateAppointmentEvents.OnNoteChange -> state = state.copy(
                note = e.note
            )

            is CreateAppointmentEvents.OnAttendeeAdded -> onAttendeeAdded(
                e.attendees
            )

            is CreateAppointmentEvents.OnRemoveAttendee -> onRemoveAttendee(e.index)
            CreateAppointmentEvents.OnGetProducts -> getProducts()
            is CreateAppointmentEvents.OnSelectProducts -> selectProduct(e.product)
            is CreateAppointmentEvents.OnAddDoctorAttendee -> addDoctorAttendee(
                e.doctors
            )

            CreateAppointmentEvents.OnCreateAppointment -> createAppointment()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createAppointment() {

        val appointments = Appointments(
            id = generateRandownString(20),
            title = state.title,
            note = state.note,
            attendees = state.attendees,
            scheduleDate = state.date,
            startTime = state.startTime,
            endTime = state.endTime,
            status = AppointmentStatus.CONFIRMED
        )
        viewModelScope.launch {
            appointmentRepository.createAppointment(appointments = appointments) {
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
                        created = it.data
                    )
                }
            }
        }

    }

    private fun addDoctorAttendee(doctors: Doctors) {
        val isAlreadyThere = state.attendees.any { it.id == doctors.id }
        if (isAlreadyThere) {
            state = state.copy(
                attendees = state.attendees.filterNot { it.id == doctors.id }
            )
            return
        }
        val attendees = Attendees(
            id = doctors.id,
            name = doctors.name,
            email = doctors.email,
            phone = doctors.phone,
            type = AttendeeType.DOCTOR
        )
        state = state.copy(
            attendees = state.attendees + attendees
        )
    }


    private fun selectProduct(product: Products) {
        //selected Product is a hashMap <String ,Products>
        state = state.copy(
            selectedProducts = state.selectedProducts + (product.id!! to product)
        )
    }

    private fun getProducts() {
        viewModelScope.launch {
            productRepository.getProductByType(ProductType.SERVICES) {
                if (it is Results.success) {
                    state = state.copy(products = it.data)
                }
            }
        }
    }

    private fun onRemoveAttendee(index: Int) {
        state = state.copy(
            attendees = state.attendees.toMutableList().apply {
                removeAt(index)
            }
        )
    }

    private fun onAttendeeAdded(attendees: Attendees) {
        state = state.copy(
            attendees = state.attendees + attendees
        )
    }

    private fun endTimeChange(
        hour : Int,
        minute : Int
    ) {
        val meridiem = if (hour < 12) {
            Meridiem.AM
        } else {
            Meridiem.PM
        }

        val adjustedHour = if (hour % 12 == 0) 12 else hour % 12

        state = state.copy(
            endTime = Hours(
                hour = adjustedHour,
                minute = minute,
                meridiem = meridiem
            )
        )
    }

    private fun startTimeChange(
        hour: Int,
        minute: Int
    ) {
        val meridiem = if (hour < 12) {
            Meridiem.AM
        } else {
            Meridiem.PM
        }

        val adjustedHour = if (hour % 12 == 0) 12 else hour % 12

        state = state.copy(
            startTime = Hours(
                hour = adjustedHour,
                minute = minute,
                meridiem = meridiem
            )
        )
    }


    private fun getDoctors() {
        viewModelScope.launch {
            doctorRepository.getDoctorsWithSchedules {
                if (it is Results.success) {
                    state = state.copy(
                        doctors = it.data
                    )
                }
            }
        }
    }
}