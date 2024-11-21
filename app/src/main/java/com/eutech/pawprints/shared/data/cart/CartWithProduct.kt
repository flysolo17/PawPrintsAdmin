package com.eutech.pawprints.shared.data.cart

import com.eutech.pawprints.products.data.products.Products


data class CartWithProduct(
    val cart : Cart,
    val product: Products,
)