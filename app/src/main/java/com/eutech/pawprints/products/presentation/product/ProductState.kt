package com.eutech.pawprints.products.presentation.product

import com.eutech.pawprints.products.data.Category
import com.eutech.pawprints.products.data.products.Products

data class ProductState(
    val isLoading : Boolean = false,
    val products: List<Products> = emptyList(),
    val categoryList : List<Category> = emptyList(),
    val errors : String ? = null,
    val selectedCategoryIndex : Int = 0,
)
