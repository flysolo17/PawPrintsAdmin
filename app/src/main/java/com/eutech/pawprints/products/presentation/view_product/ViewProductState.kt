package com.eutech.pawprints.products.presentation.view_product

import com.eutech.pawprints.products.data.products.Products

data class ViewProductState(
    val isLoading : Boolean = false,
    val product : Products ?  = Products(),
    val errors  : String ? = null,
)