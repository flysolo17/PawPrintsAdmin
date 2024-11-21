package com.eutech.pawprints.shared.data.medical

import com.eutech.pawprints.shared.presentation.utils.generateRandomNumber
import java.util.Date


data class MedicalRecord(
    val id : String = generateRandomNumber(),
    val petID: String = "",
    val diagnosis: String = "",
    val treatment: String = "",
    val vetName: String = "",
    val vetContact: String = "",
    val notes: String = "",
    val prescriptions: List<String> = listOf(),
    val date: Date = Date(),
)
