package com.eutech.pawprints.auth.data

import com.eutech.pawprints.shared.presentation.utils.generateRandownString


data class Administrator(
    val id : String ? = null,
    val name : String ? = "",
    val phone : String ? = "",
    val email : String ? = null,
    val profile : String ? = null,
)