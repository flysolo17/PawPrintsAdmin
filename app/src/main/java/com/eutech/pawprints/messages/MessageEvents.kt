package com.eutech.pawprints.messages

import com.eutech.pawprints.auth.data.Administrator
import com.eutech.pawprints.shared.data.messages.UserWithMessages


sealed interface MessageEvents  {
    data class OnSetAdmin(
        val administrator: Administrator
    ) : MessageEvents
    data object OnGetMessages : MessageEvents
    data class OnSelectConversation(
        val userWithMessages: UserWithMessages
    ): MessageEvents

    data class OnSearch(
        val text : String,
    ) : MessageEvents

    data class OnMessageChange(val message : String) : MessageEvents
    data class OnSendMessage(
        val receiver : String,
    ): MessageEvents

    data class OnSeen(
        val messages : List<String>
    ) : MessageEvents

}