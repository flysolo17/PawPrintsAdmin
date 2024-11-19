package com.eutech.pawprints.appointments.presentation.create_appointment

import com.eutech.pawprints.appointments.data.appointment.Attendees
import com.eutech.pawprints.doctors.data.Doctors
import com.eutech.pawprints.products.data.products.Products


sealed interface CreateAppointmentEvents  {
    data object OnGetDoctors : CreateAppointmentEvents
    data class OnDateChange(val date : String) : CreateAppointmentEvents
    data class OnTitleChange(val title : String) : CreateAppointmentEvents
    data class OnStartTimeChange(val hour : Int,val minute : Int) : CreateAppointmentEvents
    data class OnEndTimeChange(val hour : Int,val minute : Int) : CreateAppointmentEvents

    data class OnNoteChange(val note : String) : CreateAppointmentEvents
    data class OnAttendeeAdded(val attendees: Attendees) : CreateAppointmentEvents
    data class OnRemoveAttendee(val index : Int) : CreateAppointmentEvents
    data object OnGetProducts : CreateAppointmentEvents
    data class OnSelectProducts(val product : Products) : CreateAppointmentEvents
    data class OnAddDoctorAttendee(val doctors: Doctors) : CreateAppointmentEvents
    data object OnCreateAppointment : CreateAppointmentEvents
}