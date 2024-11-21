package com.eutech.pawprints.appointments.domain


import com.eutech.pawprints.appointments.data.appointment.AppointmentStatus
import com.eutech.pawprints.appointments.data.appointment.AppointmentWithAttendeesAndPets
import com.eutech.pawprints.appointments.data.appointment.Appointments
import com.eutech.pawprints.shared.presentation.utils.Results
import org.threeten.bp.LocalDate
import java.util.Date


interface AppointmentRepository {
    suspend fun createAppointment(
        appointments: Appointments,
        result : (Results<String>) -> Unit
    )
    suspend fun getAppointment(
        localDate: LocalDate,
        result: (Results<List<Appointments>>) -> Unit
    )
    suspend fun updateAppointmentStatus(
        id : String,
        status : AppointmentStatus,
        result: (Results<String>) -> Unit
    )

    suspend fun getAppointmentWithAttendeesAndPets(
        localDate: LocalDate,
        result: (Results<List<AppointmentWithAttendeesAndPets>>) -> Unit
    )

    suspend fun cancelAppointmentDueToPastDate(
        appointments : List<AppointmentWithAttendeesAndPets>,
        result: (Results<String>) -> Unit
    )

    suspend fun getAllAppointments(result: (Results<List<AppointmentWithAttendeesAndPets>>) -> Unit)

    suspend fun getAppointmentByPetID(petID : String,result: (Results<List<Appointments>>) -> Unit)

    suspend fun updateAppointmentStatus(
        status : AppointmentStatus,
        appointments: Appointments,
        result: (Results<String>) -> Unit
    )


    suspend fun getMyAppointments(
        userID : String,
        result: (Results<List<Appointments>>) -> Unit
    )
}