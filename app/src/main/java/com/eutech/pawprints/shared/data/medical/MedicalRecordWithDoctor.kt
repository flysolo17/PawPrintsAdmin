package com.eutech.pawprints.shared.data.medical

import com.eutech.pawprints.doctors.data.Doctors


data class MedicalRecordWithDoctor(
    val record: MedicalRecord,
    val doctors: Doctors ? = null
)