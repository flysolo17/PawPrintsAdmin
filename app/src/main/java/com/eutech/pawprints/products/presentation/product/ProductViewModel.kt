package com.eutech.pawprints.products.presentation.product

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.eutech.pawprints.products.data.Category
import com.eutech.pawprints.products.domain.CategoryRepository
import com.eutech.pawprints.products.domain.ProductRepository
import com.eutech.pawprints.shared.presentation.routes.MainRouter
import com.eutech.pawprints.shared.presentation.routes.ProductRouter
import com.eutech.pawprints.shared.presentation.utils.Results
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
     private val productRepository: ProductRepository,
     private val categoryRepository: CategoryRepository
) : ViewModel() {
    var state by mutableStateOf(ProductState())
    init {
        events(ProductEvents.OnGetAllCategories)
        events(ProductEvents.OnGetProducts)
    }
    fun events(e : ProductEvents) {
        when(e) {
            is ProductEvents.OnCreateProduct -> navigateToCreateProduct(e.navHostController)
            is ProductEvents.OnGetProducts -> getProducts()
            ProductEvents.OnGetAllCategories -> getCategories()
            is ProductEvents.OnCategoryClick -> state = state.copy(selectedCategoryIndex = e.index)
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

    private fun navigateToCreateProduct(navHostController: NavHostController) {
        navHostController.navigate(ProductRouter.CreateProduct.route)
    }

    private fun getProducts() {
        viewModelScope.launch {
            productRepository.getAllProducts {
              state =  when(it) {
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
                        products = it.data
                    )
                }
            }
        }
    }
}