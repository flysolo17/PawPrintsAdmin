package com.eutech.pawprints.doctors.presentation.create_doctor

import android.net.Uri
import androidx.compose.ui.graphics.Color
import com.eutech.pawprints.appointments.presentation.create_appointment.CreateAppointmentEvents

//val id : String ? = null,
//var profile : String ? = null,
//val name : String ? = null,
//val email : String ? = null,
//val phone : String ? = null,
//val createdAt : Date = Date()
sealed interface CreateDoctorEvents {
    data class OnProfileChanged(val uri : Uri ? ) : CreateDoctorEvents
    data class OnNameChanged(val name : String) : CreateDoctorEvents
    data class OnEmailChanged(val email: String) : CreateDoctorEvents
    data class OnPhoneChanged(val phone : String) : CreateDoctorEvents
    data object OnCreateDoctor : CreateDoctorEvents
    data class OnColorSelected(val color : Int) : CreateDoctorEvents

}