package com.eutech.pawprints.shared.domain.repository.messages

import com.eutech.pawprints.shared.data.messages.Message
import com.eutech.pawprints.shared.data.messages.UserWithMessages
import com.eutech.pawprints.shared.presentation.utils.Results


sealed interface MessagesRepository {
    suspend fun getAllMessages(
        result : (Results<List<UserWithMessages>>) -> Unit
   )

    suspend fun sendMessage(
        message: Message,
        result: (Results<String>) -> Unit
    )
    suspend fun getUnseenMessages(
        result: (Results<List<Message>>) -> Unit
    )

    suspend fun seenMessages(
        messages : List<String>
    )
}