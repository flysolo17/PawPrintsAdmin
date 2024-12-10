package com.eutech.pawprints.orders

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eutech.pawprints.shared.data.transactions.Payment
import com.eutech.pawprints.shared.data.transactions.Transaction
import com.eutech.pawprints.shared.data.transactions.TransactionStatus
import com.eutech.pawprints.shared.data.transactions.TransactionWithUser
import com.eutech.pawprints.shared.data.users.Users
import com.eutech.pawprints.shared.domain.repository.transactions.TransactionRepository
import com.eutech.pawprints.shared.presentation.utils.Results
import com.eutech.pawprints.transactions.TransactionEvents
import com.eutech.pawprints.transactions.TransactionsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class OrderViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository
) : ViewModel() {
    var state by mutableStateOf(OrderState())
    init {
        events(OrderEvents.OnGetTransactions)
    }
    fun events(e : OrderEvents) {
        when(e) {
            OrderEvents.OnGetTransactions -> getTransactions()
            is OrderEvents.OnSearching -> search(e.text)
            is OrderEvents.OnSelectTransaction -> state = state.copy(
                selectedTransaction = e.transactionWithUser
            )

            is OrderEvents.OnAddPayment -> addPayment(e.id,e.user,e.amountReceived)
            is OrderEvents.OnUpdateTransactionStatus -> updateStatus(e.id, e.user,e.status,e.transaction)
            is OrderEvents.OnSelectTransactionForPayment -> selectTransactionForPayment(
                e.transactionWithUser
            )

            is OrderEvents.OnAmountReceivedChange -> state = state.copy(
                amountReceived = e.text
            )
        }
    }

    private fun selectTransactionForPayment(transactionWithUser: TransactionWithUser?) {
        if (transactionWithUser == null) {
            state = state.copy(
                amountReceived = "0.00"
            )
        }
        state = state.copy(
            selectedTransactionForPayment =transactionWithUser
        )
    }

    private fun addPayment(id: String,user: Users?,amountReceived : Double) {
        viewModelScope.launch {
            transactionRepository.addPayment(
                id,
                user,
                amountReceived
            ) {
                state = when(it) {
                    is Results.failuire -> {
                        events(OrderEvents.OnSelectTransactionForPayment(null))
                        state.copy(
                            isPaying = false,
                            errors = it.message,
                        )
                    }
                    is Results.loading -> state.copy(
                        isPaying = true,
                        errors = null
                    )
                    is Results.success -> {
                        events(OrderEvents.OnSelectTransactionForPayment(null))
                        state.copy(
                            isPaying = false,
                            errors = null,
                            isPaymentUpdated = it.data
                        )

                    }
                }
            }
        }
    }

    private fun updateStatus(
        id: String,
        user: Users?,
        status: TransactionStatus,
        transaction: Transaction
    ) {
        viewModelScope.launch {
            transactionRepository.updateStatus(id,user,status,transaction) {
                state = when(it) {
                    is Results.failuire -> state.copy(
                        isUpdating = false,
                        errors = it.message
                    )
                    is Results.loading -> state.copy(
                        isUpdating = false,
                        errors = null,
                    )
                    is Results.success -> state.copy(
                        isUpdating = false,
                        errors = null,
                        isUpdated = it.data
                    )
                }
            }
        }
    }

    private fun search(text: String) {
        val filteredList = if (text.isBlank()) {
            state.transactions
        } else {
            state.transactions.filter { transaction ->
                transaction.transaction.id?.contains(text, ignoreCase = true) == true ||
                transaction.users?.name?.contains(text, ignoreCase = true) == true
            }
        }
        state = state.copy(searchText = text, filteredTransaction = filteredList)
    }

    private fun getTransactions() {
        viewModelScope.launch {
            transactionRepository.getAllOnGoingTransaction {
                state = when(it) {
                    is Results.failuire -> state.copy(
                        isLoading = false,
                        errors = it.message
                    )
                    is Results.loading -> state.copy(
                        isLoading = false,
                        errors = null,
                    )
                    is Results.success -> state.copy(
                        isLoading = false,
                        errors = null,
                        transactions = it.data,
                        filteredTransaction = it.data
                    )
                }
            }
        }
    }
}