package com.eutech.pawprints.shared.data.medical

import com.eutech.pawprints.shared.presentation.utils.generateRandomNumber
import java.util.Date


data class Prescription(
    val id : String = generateRandomNumber(),
    val petID: String = "",
    val medicationName: String = "",
    val dosage: String = "",
    val duration: String = "",
    val instructions: String = "",
    val notes: String = "",
    val issuedDate: Date = Date(),
    val expirationDate : Date ? =null
)
