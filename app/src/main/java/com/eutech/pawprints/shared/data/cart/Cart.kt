package com.eutech.pawprints.shared.data.cart

import java.util.Date


data class Cart(
    val id: String? = null,
    val userID: String? = null,
    val productID: String? = null,
    val quantity: Int? = null,
    val price: Double? = null,
    val addedAt: Date = Date(),
)
