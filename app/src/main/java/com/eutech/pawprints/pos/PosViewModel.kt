package com.eutech.pawprints.pos

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eutech.pawprints.products.data.Category
import com.eutech.pawprints.products.data.products.Products
import com.eutech.pawprints.products.data.products.toItem
import com.eutech.pawprints.products.domain.CategoryRepository
import com.eutech.pawprints.products.domain.ProductRepository
import com.eutech.pawprints.products.presentation.product.ProductEvents
import com.eutech.pawprints.shared.data.transactions.Transaction
import com.eutech.pawprints.shared.data.transactions.computeTotal
import com.eutech.pawprints.shared.domain.repository.transactions.TransactionRepository
import com.eutech.pawprints.shared.presentation.utils.Results
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class PosViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val transactionRepository: TransactionRepository,
    private val categoryRepository : CategoryRepository
) : ViewModel() {
    var state by mutableStateOf(PosState())
    init {
        events(PosEvents.OnGetAllCategories)
        events(PosEvents.OnGetProducts)
    }
    fun events(e : PosEvents) {
        when(e) {
            is PosEvents.OnGetProducts -> getProducts()
            PosEvents.OnGetAllCategories -> getCategories()
            is PosEvents.OnRemoveItem -> removeItem(e.index)
            is PosEvents.OnSelectProduct ->selectProduct(e.product)
            is PosEvents.OnCategoryClick -> state = state.copy(selectedCategoryIndex = e.index)
            is PosEvents.DecreaseQuantity -> decrease(e.index)
            is PosEvents.IncreaseQuantity -> increase(e.index)
            PosEvents.ComputeTotal -> computeTotal()
            PosEvents.Submit -> state = state.copy(
                showPaymentDialog = !state.showPaymentDialog
            )

            is PosEvents.SaveTransaction -> save(e.transaction)
            is PosEvents.OnAmountReceivedChange ->state = state.copy(
                amountReceived = e.text
            )
        }
    }

    private fun save(transaction: Transaction) {
        viewModelScope.launch {
            transactionRepository.createTransaction(transaction) {
                state = when(it) {
                    is Results.failuire -> state.copy(
                        isSubmitting = false,
                        errors = it.message
                    )
                    is Results.loading -> state.copy(
                        isSubmitting = false,
                        errors = null,
                    )
                    is Results.success -> state.copy(
                        isSubmitting = false,
                        errors = null,
                        isSubmitted = it.data,
                        showPaymentDialog = false,
                        amountReceived = "",
                        items = emptyList()
                    )
                }
            }
            delay(1000)
            state = state.copy(
                isSubmitted = null
            )
        }
    }

    private fun computeTotal() {
        state = state.copy(
            total =state.items.computeTotal()
        )
    }

    private fun decrease(index: Int) {
        val items = state.items.toMutableList()
        if (index in items.indices) {
            val item = items[index]
            if (item.quantity!! > 0) {
                items[index] = item.copy(quantity = item.quantity - 1)
            }
            if (items[index].quantity == 0) {
                items.removeAt(index)
            }
            state = state.copy(items = items)
        }
    }

    private fun increase(index: Int) {
        val items = state.items.toMutableList()
        if (index in items.indices) {
            val item = items[index]
            val product = state.products.firstOrNull { it.id == item.productID }
            if (product != null && item.quantity!! < product.quantity) {
                items[index] = item.copy(quantity = item.quantity + 1)
            }
            state = state.copy(items = items)
        }
    }

    private fun removeItem(index: Int) {
        val items = state.items.toMutableList()
        if (index in items.indices) {
            items.removeAt(index)
            state = state.copy(items = items)
        }
    }

    private fun selectProduct(product: Products) {
        val item = product.toItem()
        val currentItems = state.items.toMutableList()
        if (item !in currentItems) {
            currentItems.add(item)
            state = state.copy(items = currentItems)
        }
    }

    private fun getCategories() {
        viewModelScope.launch {
            categoryRepository.getAllCategory {
                when(it) {
                    is Results.failuire -> {
                        state = state.copy(
                            errors = it.message
                        )
                    }
                    is Results.loading -> {
                        state = state.copy(
                            errors =  null
                        )
                    }
                    is Results.success -> {
                        val categories = mutableListOf<Category>()
                        categories.add(Category(name = "All"))
                        categories.addAll(it.data)
                        state = state.copy(
                            categoryList = categories,
                            errors =  null
                        )
                    }
                }
            }
        }
    }
    private fun getProducts() {
        viewModelScope.launch {
            productRepository.getAllProducts {
                state = when(it) {
                    is Results.failuire -> state.copy(
                        isLoading = false,
                        errors = it.message,

                    )
                    is Results.loading -> state.copy(
                        isLoading = true,
                        errors = null
                    )
                    is Results.success -> state.copy(
                        isLoading = false,
                        errors = null,
                        products = it.data
                    )
                }
            }
        }
    }
}