package com.eutech.pawprints.appointments.presentation.appointment

import android.icu.util.LocaleData
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.ui.text.TextStyle

import com.eutech.pawprints.appointments.data.appointment.AppointmentStatus
import com.eutech.pawprints.appointments.data.appointment.Appointments


import java.util.Locale
@RequiresApi(Build.VERSION_CODES.O)
data class AppointmentState(
    val isLoading : Boolean = false,
    val appointments: List<Appointments> = emptyList(),
    val errors : String ? = null,
    val selectedTab : AppointmentStatus = AppointmentStatus.CONFIRMED
)


