package com.eutech.pawprints.appointments.presentation.create_appointment


import com.eutech.pawprints.appointments.data.appointment.Attendees
import com.eutech.pawprints.doctors.data.DoctorWithSchedules
import com.eutech.pawprints.schedule.data.Hours
import com.eutech.pawprints.products.data.products.Products
import com.eutech.pawprints.shared.presentation.utils.toExpireFormat
import java.util.Date

data class CreateAppointmentState(
    val isLoading: Boolean = false,
    val doctors: List<DoctorWithSchedules> = emptyList(),

    val date: String = Date().toExpireFormat(),
    val title: String = "",
    val note: String = "",


    val startTime: Hours = Hours(),
    val endTime: Hours = Hours(),
    val attendees: List<Attendees> = emptyList(),
    val products: List<Products> = emptyList(),
    val selectedProducts: Map<String, Products> = HashMap(),
    val errors : String ? = null,
    val created : String ? = null
)




