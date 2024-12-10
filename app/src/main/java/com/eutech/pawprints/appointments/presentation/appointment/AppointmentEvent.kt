package com.eutech.pawprints.appointments.presentation.appointment

import android.content.Context
import com.eutech.pawprints.appointments.data.appointment.AppointmentStatus
import com.eutech.pawprints.appointments.data.appointment.Appointments
import com.eutech.pawprints.home.presentation.HomeEvents


sealed interface AppointmentEvent {
    data object OnGetAllAppointment : AppointmentEvent
    data class OnTabSelected(val tab : Int) : AppointmentEvent



    data class OnUpdateAppointment(val status : AppointmentStatus,val appointments: Appointments) :
        AppointmentEvent
}
