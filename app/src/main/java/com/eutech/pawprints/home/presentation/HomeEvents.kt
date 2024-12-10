package com.eutech.pawprints.home.presentation

import com.eutech.pawprints.appointments.data.appointment.AppointmentStatus
import com.eutech.pawprints.appointments.data.appointment.AppointmentWithAttendeesAndPets
import com.eutech.pawprints.appointments.data.appointment.Appointments
import com.eutech.pawprints.products.data.products.Products
import com.eutech.pawprints.products.presentation.product.ProductEvents
import org.threeten.bp.LocalDate

sealed interface  HomeEvents {
    data class OnGetSchedules(val localDate: LocalDate) : HomeEvents
    data class OnDateChange(val localDate: LocalDate) : HomeEvents
    data class OnGetAppointments(val localDate: LocalDate) : HomeEvents
    data class OnUpdateAppointment(val status : AppointmentStatus,val appointments: Appointments) : HomeEvents
    data class OnAutoCancelAppointments(val appointments:  List<AppointmentWithAttendeesAndPets>): HomeEvents

    data object GetOnlineOrders : HomeEvents
}