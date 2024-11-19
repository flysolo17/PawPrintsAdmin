package com.eutech.pawprints.products.data.products

import java.util.Date

data class Discount(
    val value : Double = 0.00,
    val expiration : Date? = null,
)
