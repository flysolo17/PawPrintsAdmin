package com.eutech.pawprints.products.presentation.view_product

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.eutech.pawprints.products.domain.ProductRepository
import com.eutech.pawprints.shared.presentation.utils.Results
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel

class ViewProductViewModel @Inject constructor(
     private val productRepository: ProductRepository
) : ViewModel() {
    var state by mutableStateOf(ViewProductState())
    fun events(e : ViewProductEvents) {
        when(e) {
            is ViewProductEvents.OnGetProductByID -> getProductByID(e.productID)
            is ViewProductEvents.OnDeleteProduct -> deleteProduct(e.id,e.context,e.navHostController)
            is ViewProductEvents.OnAddStocks -> addProducts(e.id
            ,e.quantity,e.date)
        }
    }

    private fun addProducts(id: String, quantity: Int, date: Date?) {
        viewModelScope.launch {
            productRepository.addProduct(id,quantity,date) {
                state = when(it){
                    is Results.failuire -> state.copy(
                        isAddingStocks = false,
                        errors = it.message,
                    )
                    is Results.loading -> state.copy(
                        isAddingStocks = true,
                        errors = null,
                    )
                    is Results.success -> {
                        state.copy(isAddingStocks = false, errors = null, isAdded = it.data)
                    }
                }
            }
            delay(1000)
            state = state.copy(isAdded = null)
        }
    }

    private fun deleteProduct(id: String,context : Context, navHostController: NavHostController) {
        viewModelScope.launch {
            productRepository.deleteProduct(id) {
                state = when(it){
                    is Results.failuire -> state.copy(
                        isLoading = false,
                        errors = it.message,
                    )
                    is Results.loading -> state.copy(
                        isLoading = true,
                        errors = null,
                    )
                    is Results.success -> {
                        Toast.makeText(context,it.data,Toast.LENGTH_SHORT).show()
                        navHostController.popBackStack()
                        state.copy(isLoading = false)
                    }
                }
            }
        }
    }

    private fun getProductByID(productID: String) {
        viewModelScope.launch {
            productRepository.getProductByID(productID) {
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
                        product = it.data
                    )
                }
            }
        }
    }
}