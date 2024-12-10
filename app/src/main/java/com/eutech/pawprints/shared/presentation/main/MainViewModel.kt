package com.eutech.pawprints.shared.presentation.main

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eutech.pawprints.shared.domain.repository.messages.MessagesRepository
import com.eutech.pawprints.shared.presentation.utils.Results
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val messagesRepository: MessagesRepository
) : ViewModel() {
    var state by mutableStateOf(MainState())
    init {
        events(MainEvents.OnGetUnseenMessages)
    }
    fun events(e : MainEvents) {
        when(e) {
            MainEvents.OnGetUnseenMessages -> getUnseenMessages()
        }
    }

    private fun getUnseenMessages() {
        viewModelScope.launch {
            messagesRepository.getUnseenMessages {
                state = when(it) {
                    is Results.failuire -> state.copy(
                        isLoading = false,
                        errors = it.message,
                    )
                    is Results.loading ->state.copy(
                        isLoading = false,
                        errors = null,

                    )
                    is Results.success -> state.copy(
                        isLoading = false,
                        errors = null,
                        unseenMessages = it.data
                    )
                }
            }
        }
    }
}