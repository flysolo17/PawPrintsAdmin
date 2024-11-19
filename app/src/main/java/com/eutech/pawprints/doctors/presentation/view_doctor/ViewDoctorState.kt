package com.eutech.pawprints.doctors.presentation.view_doctor

import com.eutech.pawprints.doctors.data.Doctors

data class ViewDoctorState(
    val isLoading : Boolean = false,

    val doctor : Doctors? = null,

    val errors : String ? = null,
)
