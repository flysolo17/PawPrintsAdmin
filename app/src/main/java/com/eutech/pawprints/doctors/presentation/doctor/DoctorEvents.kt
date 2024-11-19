package com.eutech.pawprints.doctors.presentation.doctor

import com.eutech.pawprints.doctors.data.Doctors


sealed interface DoctorEvents  {
    data object OnGetAllDoctors : DoctorEvents
    data class OnDeleteDoctor(val doctors: Doctors) : DoctorEvents
}