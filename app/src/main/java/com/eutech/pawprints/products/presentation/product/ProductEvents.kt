package com.eutech.pawprints.products.presentation.product

import androidx.navigation.NavHostController
import com.eutech.pawprints.products.presentation.create_product.CreateProductEvents

sealed interface ProductEvents  {
    data class OnCreateProduct(val navHostController: NavHostController) : ProductEvents
    data object OnGetProducts : ProductEvents
    data object OnGetAllCategories : ProductEvents
    data class OnCategoryClick(val index : Int) : ProductEvents

}