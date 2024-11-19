package com.eutech.pawprints.doctors.presentation.view_doctor

import android.content.Context
import com.chargemap.compose.numberpicker.Hours
import com.kizitonwose.calendar.core.WeekDay
import java.time.DayOfWeek
import java.time.LocalTime

sealed interface ViewDoctorEvents {
    data class OnGetDoctorInfo(val doctorID : String) : ViewDoctorEvents

}