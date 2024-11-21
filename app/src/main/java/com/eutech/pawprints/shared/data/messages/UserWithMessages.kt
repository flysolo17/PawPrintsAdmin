package com.eutech.pawprints.shared.data.messages

import com.eutech.pawprints.shared.data.users.Users


data class UserWithMessages(
    val users: Users ? = null,
    val messages : List<Message> = emptyList()
)