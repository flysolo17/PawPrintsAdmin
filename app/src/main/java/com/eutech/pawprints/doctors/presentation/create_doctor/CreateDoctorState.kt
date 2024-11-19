package com.eutech.pawprints.doctors.presentation.create_doctor

import android.net.Uri

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.eutech.pawprints.shared.presentation.utils.TextFieldData
import java.util.Date



data class CreateDoctorState(
    val isLoading : Boolean = false,

    val name : TextFieldData = TextFieldData(),
    val email : TextFieldData = TextFieldData(),
    val phone : TextFieldData = TextFieldData(),
    val profile : Uri? = null,

    val errors : String ? = null,
    val created : String ? = null,
    val tag : Int = Color.Blue.toArgb()
)