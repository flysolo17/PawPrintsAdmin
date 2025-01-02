package com.eutech.pawprints.products.presentation.edit_product

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eutech.pawprints.products.data.products.Products
import com.eutech.pawprints.products.domain.CategoryRepository
import com.eutech.pawprints.products.domain.ProductRepository
import com.eutech.pawprints.products.presentation.create_product.FormField
import com.eutech.pawprints.shared.presentation.utils.Results
import com.eutech.pawprints.shared.presentation.utils.displayDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import kotlin.math.exp


@HiltViewModel
class EditProductViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val categoryRepository: CategoryRepository
) : ViewModel() {
    var state by mutableStateOf(EditProductState())
    init {
        events(EditProductEvents.OnGetAllCategories)
    }
    fun events(e : EditProductEvents) {
        when(e) {
            EditProductEvents.OnGetAllCategories -> getCategories()
            is EditProductEvents.OnImageChanged -> state = state.copy(image = e.image)
            is EditProductEvents.OnProductTypeChange -> state = state.copy(
                selectedProductType = e.type
            )
            is EditProductEvents.OnSelectCategory ->  state=state.copy(selectedCategory = e.category)
            is EditProductEvents.OnTextFieldChange -> formFieldChange(e.text,e.form)
            is EditProductEvents.OnSave -> save(e.oldProduct)
            is EditProductEvents.OnSetProduct -> setProduct(e.products)
            is EditProductEvents.OnExpirationSave -> state = state.copy(expiration = e.date)
            is EditProductEvents.OnGetProductByID -> getProductByID(e.productID)
            is EditProductEvents.OnDeleteCategory -> deleteCategory(id = e.id)
        }
    }

    private fun setProduct(products: Products) {
        val category = state.categoryList.firstOrNull { it.id == products.categoryID }

        state = state.copy(
            name = state.name.copy(
                value = products.name ?: "",
            ),
            selectedProductType = products.type,
            selectedCategory = category,
            desc = state.desc.copy(
                value = products.description ?: "",
            ),
            features = state.features.copy(
                value = products.features ?: "",
            ),
            contents = state.contents.copy(
                value = products.contents ?: "",
            ),
            quantity = state.quantity.copy(
                value = products.quantity.toString(),
            ),
            cost = state.cost.copy(
                value = products.cost.toString(),
            ),
            price = state.price.copy(
                value = products.price.toString(),
            ),
            expiration = products.expiration?.displayDate() ?: "",
        )
    }
    private fun deleteCategory(id: String) {
        viewModelScope.launch {
            categoryRepository.deleteCategory(id) {

            }
        }
    }

    private fun save(products: Products) {
        val expiry = try {
            SimpleDateFormat("MMM, dd yyyy", Locale.getDefault()).parse(state.expiration.toString())
        } catch (e: Exception) {
            null
        }

        val newProduct = Products(
            id = products.id,
            image = products.image,
            name = state.name.value,
            description = state.desc.value,
            features = state.features.value,
            contents = state.contents.value,
            quantity = state.quantity.value.toIntOrNull() ?: 0,
            cost = state.cost.value.toDoubleOrNull() ?: 0.0,
            price = state.price.value.toDoubleOrNull() ?: 0.0,
            expiration = expiry,
            categoryID = state.selectedCategory?.id,
            type = state.selectedProductType,
            stocks = products.stocks,
            updatedAt = Date(),
            createdAt = products.createdAt
        )
        viewModelScope.launch {
            productRepository.createProduct(newProduct, uri = state.image) { result ->
                state = when (result) {
                    is Results.success -> state.copy(
                        isLoading = false,
                        isSaved = result.data,
                        errors = null
                    )
                    is Results.failuire -> state.copy(
                        isLoading = false,
                        errors = result.message
                    )
                    is Results.loading -> state.copy(
                        isLoading = true,
                        errors = null
                    )
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
                    is Results.success -> {
                        it.data?.let {
                            events(EditProductEvents.OnSetProduct(it))
                        }

                        state.copy(
                            isLoading = false,
                            errors = null,
                            products = it.data
                        )
                    }
                }
            }
        }
    }
}