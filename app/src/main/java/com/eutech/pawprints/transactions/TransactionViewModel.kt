package com.eutech.pawprints.transactions

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eutech.pawprints.shared.domain.repository.inbox.InboxRepository
import com.eutech.pawprints.shared.domain.repository.transactions.TransactionRepository
import com.eutech.pawprints.shared.domain.repository.users.UsersRepository
import com.eutech.pawprints.shared.presentation.utils.Results
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val userRepository : UsersRepository,
    private val inboxRepository: InboxRepository
) : ViewModel() {
    var state by mutableStateOf(TransactionsState())
    init {
        events(TransactionEvents.OnGetAllTransactions)
    }
    fun events(e : TransactionEvents) {
        when(e) {
            TransactionEvents.OnGetAllTransactions -> getTransactions()
            is TransactionEvents.OnSearch -> search(e.text)
            is TransactionEvents.OnSelectTranaction -> state = state.copy(
                selectedTransaction = e.transaction
            )

            is TransactionEvents.OnGetTransactionInbox -> getTransactionInbox(e.transactionID)
            is TransactionEvents.OnGetUser -> getUser(e.userID)
        }
    }

    private fun getUser(userID: String) {
        viewModelScope.launch {
            userRepository.getUserByID(userID) { result ->
                state = when (result) {
                    is Results.failuire -> {
                        state.copy(
                            userState = state.userState.copy(
                                isLoading = false,
                                errors = result.message
                            )
                        )
                    }
                    is Results.loading -> {
                        state.copy(
                            userState = state.userState.copy(
                                isLoading = true,
                                errors = null
                            )
                        )
                    }
                    is Results.success -> {
                        state.copy(
                            userState = state.userState.copy(
                                isLoading = false,
                                errors = null,
                                users = result.data
                            )
                        )
                    }
                }
            }
        }
    }

    private fun getTransactionInbox(transactionID: String) {
        viewModelScope.launch {
            inboxRepository.getInboxByCollectionID(transactionID) { result ->
                state = when (result) {
                    is Results.failuire -> {
                        state.copy(
                            inboxState = state.inboxState.copy(
                                isLoading = false,
                                errors = result.message
                            )
                        )
                    }
                    is Results.loading -> {
                        state.copy(
                            inboxState = state.inboxState.copy(
                                isLoading = true,
                                errors = null
                            )
                        )
                    }
                    is Results.success -> {
                        state.copy(
                            inboxState = state.inboxState.copy(
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

    private fun search(text: String) {
        val filteredList = if (text.isBlank()) {
            state.transactions
        } else {
            state.transactions.filter { transaction ->
                transaction.id?.contains(text, ignoreCase = true) == true
            }
        }
        state = state.copy(searchText = text, filteredTransactions = filteredList)
    }
    private fun getTransactions() {
        viewModelScope.launch {
            transactionRepository.getAllTransactions {
                state = when(it) {
                    is Results.failuire -> state.copy(
                        isLoading = false,
                        errors = it.message
                    )
                    is Results.loading -> state.copy(
                        isLoading = true,
                        errors = null,
                    )
                    is Results.success -> state.copy(
                        isLoading = false,
                        errors = null,
                        transactions = it.data,
                        filteredTransactions = it.data
                    )
                }
            }
        }
    }
}