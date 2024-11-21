package com.eutech.pawprints.users

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eutech.pawprints.appointments.domain.AppointmentRepository
import com.eutech.pawprints.shared.domain.repository.inbox.InboxRepository
import com.eutech.pawprints.shared.domain.repository.pets.PetRepository
import com.eutech.pawprints.shared.domain.repository.transactions.TransactionRepository
import com.eutech.pawprints.shared.domain.repository.users.UsersRepository
import com.eutech.pawprints.shared.presentation.utils.Results
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UsersViewModel @Inject constructor(
    private val usersRepository: UsersRepository,
    private val petRepository: PetRepository,
    private val inboxRepository: InboxRepository,
    private val appointmentRepository: AppointmentRepository,
    private val transactionRepository : TransactionRepository
) : ViewModel() {
    var state by mutableStateOf(UserState())
    init {
        events(UsersEvents.OnGetAllUsers)
    }

    fun events(e : UsersEvents) {
        when(e) {
            UsersEvents.OnGetAllUsers -> getUsers()
            is UsersEvents.OnSelectUser -> state = state.copy(selectedUser = e.users)
            is UsersEvents.OnSearch -> search(e.text)
            is UsersEvents.OnGetPets -> getMyPets(e.userID)
            is UsersEvents.OnGetInbox -> getMyInbox(e.userID)
            is UsersEvents.OnGetAppointments -> getAppointments(e.userID)
            is UsersEvents.OnGetTransactions -> getTransactions(e.userID)
        }
    }

    private fun getTransactions(userID: String) {
        viewModelScope.launch {
            transactionRepository.getMyTransactions(userID) { result ->
                state = when (result) {
                    is Results.failuire -> {
                        state.copy(
                            transactionTabState = state.transactionTabState.copy(
                                isLoading = false,
                                errors = result.message
                            )
                        )
                    }
                    is Results.loading -> {
                        state.copy(
                            transactionTabState = state.transactionTabState.copy(
                                isLoading = true,
                                errors = null
                            )
                        )
                    }
                    is Results.success -> {
                        state.copy(
                            transactionTabState = state.transactionTabState.copy(
                                isLoading = false,
                                errors = null,
                                transactions = result.data
                            )
                        )
                    }
                }
            }
        }
    }

    private fun getAppointments(userID: String) {
        viewModelScope.launch {
            appointmentRepository.getMyAppointments(userID) { result ->
                state = when (result) {
                    is Results.failuire -> {
                        state.copy(
                            appointmentTabState = state.appointmentTabState.copy(
                                isLoading = false,
                                errors = result.message
                            )
                        )
                    }
                    is Results.loading -> {
                        state.copy(
                            appointmentTabState = state.appointmentTabState.copy(
                                isLoading = true,
                                errors = null
                            )
                        )
                    }
                    is Results.success -> {
                        state.copy(
                            appointmentTabState = state.appointmentTabState.copy(
                                isLoading = false,
                                errors = null,
                                appointments = result.data
                            )
                        )
                    }
                }
            }
        }
    }

    private fun getMyInbox(userID: String) {
        viewModelScope.launch {
            inboxRepository.getAllInboxByUserID(userID) { result ->
                state = when (result) {
                    is Results.failuire -> {
                        state.copy(
                            inboxTabState = state.inboxTabState.copy(
                                isLoading = false,
                                errors = result.message
                            )
                        )
                    }
                    is Results.loading -> {
                        state.copy(
                            inboxTabState = state.inboxTabState.copy(
                                isLoading = true,
                                errors = null
                            )
                        )
                    }
                    is Results.success -> {
                        state.copy(
                            inboxTabState = state.inboxTabState.copy(
                                isLoading = false,
                                errors = null,
                                inboxes = result.data
                            )
                        )
                    }
                }
            }
        }
    }

    private fun getMyPets(userID: String) {
        viewModelScope.launch {
            petRepository.getPetsByUserID(userID) { result ->
                state = when (result) {
                    is Results.failuire -> {
                        state.copy(
                            petTabState = state.petTabState.copy(
                                isLoading = false,
                                errors = result.message
                            )
                        )
                    }
                    is Results.loading -> {
                        state.copy(
                            petTabState = state.petTabState.copy(
                                isLoading = true,
                                errors = null
                            )
                        )
                    }
                    is Results.success -> {
                        state.copy(
                            petTabState = state.petTabState.copy(
                                isLoading = false,
                                errors = null,
                                pets = result.data
                            )
                        )
                    }
                }
            }
        }
    }


    private fun search(text: String) {
        val filteredList = if (text.isBlank()) {
            state.users
        } else {
            state.users.filter { user ->
                user.name?.contains(text, ignoreCase = true) == true ||
                user.email?.contains(text,ignoreCase = true) == true ||
                user.phone?.contains(text,ignoreCase = true) == true
            }
        }

        state = state.copy(searchText = text, filteredUser = filteredList)
    }

    private fun getUsers() {
        viewModelScope.launch {
            usersRepository.getAllUsers {
                state = when(it) {
                    is Results.failuire -> state.copy(
                        isLoading = false,
                        errors = it.message
                    )
                    is Results.loading -> state.copy(
                        isLoading = true,
                        errors = null
                    )
                    is Results.success -> state.copy(
                        isLoading = false,
                        errors = null,
                        users = it.data,
                        filteredUser = it.data
                    )
                }
            }
        }
    }
}