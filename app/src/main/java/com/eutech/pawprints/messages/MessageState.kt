package com.eutech.pawprints.messages

import com.eutech.pawprints.auth.data.Administrator
import com.eutech.pawprints.shared.data.messages.UserWithMessages


data class MessageState(
    val isLoading : Boolean = false,
    val userWithMessages: List<UserWithMessages> = emptyList(),
    val filteredMessages : List<UserWithMessages> = emptyList(),
    val errors : String ? =  null,
    val administrator: Administrator ? = null,

    val selectedConvo : UserWithMessages ? = null,
    val searchText : String  = "",
    val message : String = "",

    val isSending : Boolean = false,
    val messagingState: MessagingState = MessagingState()
)

data class MessagingState(
    val isLoading : Boolean = false,
    val isSent : String ? = null,
    val errors : String ? = null
)