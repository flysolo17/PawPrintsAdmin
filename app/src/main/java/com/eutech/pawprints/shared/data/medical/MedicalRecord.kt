package com.eutech.pawprints.shared.data.medical

import com.eutech.pawprints.shared.presentation.utils.generateRandomNumber
import java.util.Date


data class MedicalRecord(
    val id : String = generateRandomNumber(),
    val images : List<String> = emptyList(),
    val petID: String = "",
    val diagnosis: String = "",
    val treatment: String = "",
    val doctorID :String ? = null,
    val notes: String = "",
    val prescriptions: List<Prescription> = listOf(),
    val date: Date = Date(),
)


