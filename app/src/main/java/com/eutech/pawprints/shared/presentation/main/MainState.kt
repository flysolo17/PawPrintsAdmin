package com.eutech.pawprints.shared.presentation.main

import com.eutech.pawprints.shared.data.messages.Message


data class MainState(
    val isLoading : Boolean = false,
    val unseenMessages : List<Message> = emptyList(),
    val errors : String? = null
)