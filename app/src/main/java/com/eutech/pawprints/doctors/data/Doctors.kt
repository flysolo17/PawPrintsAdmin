package com.eutech.pawprints.doctors.data

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import java.util.Date

data class Doctors(
    val id : String ? = null,
    var profile : String ? = null,
    val name : String ? = null,
    val email : String ? = null,
    val phone : String ? = null,
    val tag : Int ? = Color.Blue.toArgb(),
    val createdAt : Date = Date()
)
