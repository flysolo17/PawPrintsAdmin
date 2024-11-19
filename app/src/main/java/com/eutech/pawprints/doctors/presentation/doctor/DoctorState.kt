package com.eutech.pawprints.doctors.presentation.doctor

import com.eutech.pawprints.doctors.data.Doctors

data class DoctorState(
    val isLoading : Boolean = false,
    val doctors : List<Doctors> = emptyList(),
    val errors : String ? = null,
    val deleteSuccess : String ? = null,
)