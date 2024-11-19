package com.eutech.pawprints.appointments.presentation.appointment

import android.content.Context
import com.eutech.pawprints.appointments.data.appointment.AppointmentStatus


sealed interface AppointmentEvent {
    data object OnGetAllAppointment : AppointmentEvent
    data class OnTabSelected(val tab : AppointmentStatus) : AppointmentEvent
    data class OnUpdateStatus(
        val id : String,
        val status: AppointmentStatus,
        val context : Context
    ) : AppointmentEvent
}