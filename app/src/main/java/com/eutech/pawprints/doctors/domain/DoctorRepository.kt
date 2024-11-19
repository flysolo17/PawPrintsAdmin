package com.eutech.pawprints.doctors.domain

import android.net.Uri
import com.eutech.pawprints.doctors.data.DoctorWithSchedules
import com.eutech.pawprints.doctors.data.Doctors
import com.eutech.pawprints.shared.presentation.utils.Results

interface DoctorRepository {
   suspend fun createDoctor(doctors: Doctors,uri: Uri ?,result: (Results<String>) -> Unit)
    suspend fun getAllDoctors(result: (Results<List<Doctors>>) -> Unit)
    suspend  fun deleteDoctor(doctors: Doctors,result: (Results<String>) -> Unit)
    suspend fun getDoctorById(doctorID : String,result: (Results<Doctors?>) -> Unit)
    suspend fun getDoctorsWithSchedules(result: (Results<List<DoctorWithSchedules>>) -> Unit)
}