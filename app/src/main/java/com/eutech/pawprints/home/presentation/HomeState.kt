package com.eutech.pawprints.home.presentation

import com.eutech.pawprints.appointments.data.appointment.AppointmentWithAttendeesAndPets
import com.eutech.pawprints.appointments.data.appointment.Appointments
import com.eutech.pawprints.auth.data.Administrator
import com.eutech.pawprints.home.data.ProductWithCart
import com.eutech.pawprints.products.data.Category
import com.eutech.pawprints.products.data.products.Products
import com.eutech.pawprints.schedule.data.ScheduleWithDoctor
import com.eutech.pawprints.shared.data.transactions.Transaction
import com.eutech.pawprints.shared.data.transactions.TransactionWithUser
import org.threeten.bp.LocalDate


data class HomeState(
    val isLoading : Boolean  = false,
    val isGettingAppointments : Boolean = false,
    val admin : Administrator ? = null,
    val errors : String ? = null,
    val selectedDate : LocalDate = LocalDate.now(),
    val schedules : List<ScheduleWithDoctor> = emptyList(),
    val appointment : List<AppointmentWithAttendeesAndPets> = emptyList(),
    val filterAppointment : String = "",
    val messages : String ? = null,

    val orders : List<TransactionWithUser> = emptyList(),

    val updatingStatus : Boolean = false,
    val isUpdated : String ? = null,



)