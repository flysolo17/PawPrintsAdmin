package com.eutech.pawprints.appointments.presentation.appointment

import android.icu.util.LocaleData
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.ui.text.TextStyle

import com.eutech.pawprints.appointments.data.appointment.AppointmentStatus
import com.eutech.pawprints.appointments.data.appointment.AppointmentWithAttendeesAndPets
import com.eutech.pawprints.appointments.data.appointment.Appointments


import java.util.Locale
@RequiresApi(Build.VERSION_CODES.O)
data class AppointmentState(
    val isLoading : Boolean = false,
    val appointments: List<AppointmentWithAttendeesAndPets> = emptyList(),

    val updatingStatus : Boolean = false,
    val isUpdated : String ? = null,
    val errors : String ? = null,
    val selectedTab : Int = 0
)


