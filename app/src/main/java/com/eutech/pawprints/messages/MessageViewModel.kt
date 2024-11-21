package com.eutech.pawprints.messages

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eutech.pawprints.shared.data.messages.Message
import com.eutech.pawprints.shared.data.messages.UserType
import com.eutech.pawprints.shared.domain.repository.messages.MessagesRepository
import com.eutech.pawprints.shared.presentation.utils.Results
import com.eutech.pawprints.shared.presentation.utils.generateRandomNumber
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MessageViewModel @Inject constructor(
    private val messagesRepository: MessagesRepository
) : ViewModel() {
    var state by mutableStateOf(MessageState())
    init {
        events(MessageEvents.OnGetMessages)
    }
    fun events(e : MessageEvents) {
        when(e) {
            MessageEvents.OnGetMessages -> getMessages()
            is MessageEvents.OnSelectConversation -> state = state.copy(
                selectedConvo = e.userWithMessages
            )

            is MessageEvents.OnSetAdmin -> state = state.copy(
                administrator = e.administrator
            )
            is MessageEvents.OnSearch -> search(e.text)
            is MessageEvents.OnMessageChange -> state = state.copy(
                message = e.message
            )
            is MessageEvents.OnSendMessage -> sendMessage(e.receiver)
        }
    }

    private fun sendMessage(receiver : String) {
        val message : Message = Message(
            id = generateRandomNumber(),
            message = state.message,
            receiver = receiver,
            sender =  "7ter7VWCmbUzZLe51b8wOraZgEy2",
            type = UserType.ADMIN,
        )
        viewModelScope.launch {
            messagesRepository.sendMessage(message = message) { result ->
                state = when (result) {
                    is Results.failuire -> {
                        state.copy(
                            messagingState = state.messagingState.copy(
                                isLoading = false,
                                errors = result.message
                            )
                        )
                    }
                    is Results.loading -> {
                        state.copy(
                            messagingState = state.messagingState.copy(
                                isLoading = true,
                                errors = null
                            )
                        )
                    }
                    is Results.success -> {
                        state.copy(
                            messagingState = state.messagingState.copy(
                                isLoading = false,
                                errors = null,
                                isSent = result.data
                            )
                        )
                    }

                }
            }

            delay(1000)
            state = state.copy(message = "",
                messagingState = state.messagingState.copy(
                    isLoading = false,
                    errors = null,
                    isSent = null
                )
            )
        }
    }

    private fun search(text: String) {
        val filtered = if (text.isNotEmpty()) {
            state.filteredMessages.filter { message ->
                message.users?.name?.contains(text, ignoreCase = true) == true
            }
        } else {
            state.userWithMessages
        }

        state = state.copy(
            searchText = text,
            filteredMessages = filtered
        )
    }

    private fun getMessages() {
        viewModelScope.launch {
            messagesRepository.getAllMessages {
                state = when(it) {
                    is Results.failuire -> state.copy(
                        isLoading = false,
                        errors = null
                    )
                    is Results.loading -> state.copy(
                        isLoading = true,
                        errors = null
                    )
                    is Results.success -> {
                        val sorted =  it.data.sortedByDescending {
                            it.messages.maxByOrNull { message -> message.createdAt }?.createdAt
                        }
                        state.copy(
                            isLoading = false,
                            errors = null,
                            filteredMessages = sorted,
                            userWithMessages = sorted
                        )
                    }
                }
            }
        }
    }
}