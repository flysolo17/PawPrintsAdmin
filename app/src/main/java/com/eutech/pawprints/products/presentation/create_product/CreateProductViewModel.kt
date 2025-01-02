package com.eutech.pawprints.products.presentation.create_product

import android.content.Context
import android.util.Patterns
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.eutech.pawprints.products.data.Category
import com.eutech.pawprints.products.domain.CategoryRepository
import com.eutech.pawprints.products.domain.ProductRepository
import com.eutech.pawprints.products.data.products.Movement
import com.eutech.pawprints.products.data.products.Products
import com.eutech.pawprints.products.data.products.StockManagement
import com.eutech.pawprints.shared.presentation.utils.Results
import com.eutech.pawprints.shared.presentation.utils.generateRandomNumber
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel

class CreateProductViewModel @Inject constructor(
     private val productRepository: ProductRepository,
     private val categoryRepository: CategoryRepository
) : ViewModel() {
    var state by mutableStateOf(CreateProductState())

    init {
        events(CreateProductEvents.OnGetAllCategories)
    }
    fun events(e : CreateProductEvents) {
        when(e) {
            is CreateProductEvents.OnImageChanged -> state = state.copy(image = e.image)
            is CreateProductEvents.OnTextFieldChange -> formFieldChange(e.text,e.form)
            CreateProductEvents.OnGetAllCategories -> getCategories()
            is CreateProductEvents.SubmitProduct -> submitProduct(e.context,e.navHostController)
            is CreateProductEvents.OnSelectCategory -> state=state.copy(selectedCategory = e.category)
            is CreateProductEvents.OnCreateCategory -> createCategory(e.category)
            is CreateProductEvents.OnProductTypeChange -> state = state.copy(
                selectedProductType = e.type
            )

            is CreateProductEvents.DeleteCategory -> deleteCategory(e.id)
        }
    }

    private fun deleteCategory(id: String) {
        viewModelScope.launch {
            categoryRepository.deleteCategory(id) {

            }
        }
    }

    private fun createCategory(category: String) {
        viewModelScope.launch {
            val c = Category(
                name = category
            )
            categoryRepository.createCategory(c) {
                state = when(it) {
                    is Results.failuire -> state.copy(
                        errors = it.message
                    )

                    is Results.loading -> state.copy(
                        errors = null
                    )

                    is Results.success -> state.copy(
                        errors = null,
                        message = it.data
                    )
                }
            }
        }
    }

    private fun submitProduct(context: Context, navHostController: NavHostController) {
        if (state.image == null) {
            Toast.makeText(context, "Please Add image", Toast.LENGTH_SHORT).show()
            return
        }
        if (state.formHasError) {
            Toast.makeText(context, "Invalid form data", Toast.LENGTH_SHORT).show()
            return
        }

        val quantity = state.quantity.value.toIntOrNull() ?: 0
        val stockList = if (quantity > 0) {
            mutableListOf(StockManagement(quantity = quantity, movement = Movement.IN))
        } else {
            mutableListOf()
        }

        val products = Products(
            id = generateRandomNumber(12),
            name = state.name.value,
            description = state.desc.value,
            contents = state.contents.value,
            categoryID = state.selectedCategory?.id,
            features = state.features.value,
            quantity = quantity,
            type = state.selectedProductType,
            stocks = stockList,
            cost = state.cost.value.toDoubleOrNull() ?: 0.00,
            price = state.price.value.toDoubleOrNull() ?: 0.00,
        )

        viewModelScope.launch {
            productRepository.createProduct(products = products, uri = state.image) { result ->
                state = when (result) {
                    is Results.failuire -> state.copy(isLoading = false, errors = result.message)
                    is Results.loading -> state.copy(isLoading = true, errors = null)
                    is Results.success -> state.copy(isLoading = false, errors = null, created = result.data)
                }
            }
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
                        state = state.copy(
                            categoryList = it.data,
                            errors =  null
                        )
                    }
                }
            }
        }
    }

    private fun formFieldChange(text: String, form: FormField) {
        when(form) {
            FormField.NAME -> {
                val hasError = text.isEmpty()
                val errorMessage = if (hasError) "Invalid name" else null
                val newName = state.name.copy(
                    value = text,
                    hasError = hasError,
                    errorMessage = errorMessage
                )
                state = state.copy(name = newName, formHasError = hasError)
            }
            FormField.DESC -> {
                val desc = state.desc.copy(value = text)
                state = state.copy(desc = desc)
            }
            FormField.FEATURES -> {
                val features = state.features.copy(value = text)
                state = state.copy(features = features)
            }
            FormField.CONTENTS -> {
                val hasError = text.isEmpty()
                val errorMessage = if (hasError) "Invalid content" else null
                val contents = state.contents.copy(
                    value = text,
                    hasError = hasError,
                    errorMessage = errorMessage
                )
                state = state.copy(contents = contents, formHasError = hasError)
            }
            FormField.QUANTITY -> {
                val hasError = text.isEmpty() || text.toIntOrNull() == null || text.toInt() <= 0
                val errorMessage = if (hasError) "Invalid quantity" else null
                val quantity = state.quantity.copy(
                    value = text,
                    hasError = hasError,
                    errorMessage = errorMessage
                )
                state = state.copy(quantity = quantity, formHasError = hasError)
            }
            FormField.COST -> {
                val hasError = text.isEmpty() || text.toDoubleOrNull() == null || text.toDouble() <= 0.0
                val errorMessage = if (hasError) "Invalid cost" else null
                val cost = state.cost.copy(
                    value = text,
                    hasError = hasError,
                    errorMessage = errorMessage
                )
                state = state.copy(cost = cost, formHasError = hasError)
            }
            FormField.PRICE -> {
                val hasError = text.isEmpty() || text.toDoubleOrNull() == null || text.toDouble() <= 0.0 || (state.cost.value.toDoubleOrNull() != null && text.toDouble() < state.cost.value.toDouble())
                val errorMessage = if (hasError) "Invalid price" else null
                val price = state.price.copy(
                    value = text,
                    hasError = hasError,
                    errorMessage = errorMessage
                )
                state = state.copy(price = price, formHasError = hasError)
            }
        }
    }
}