package com.eutech.pawprints.auth.presentation.profile

import com.eutech.pawprints.auth.data.Administrator


data class ProfileState(
    val isLoading : Boolean = false,
    val administrator: Administrator ? = null,
    val errors : String ? = null,
)