package com.eutech.pawprints.home.data

import com.eutech.pawprints.products.data.products.Products


data class ProductWithCart(
    val product : Products,
    val cartItems: CartItems
)