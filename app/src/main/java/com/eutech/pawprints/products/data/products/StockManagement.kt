package com.eutech.pawprints.products.data.products

import java.util.Date

data class StockManagement(
    val message : String = "",
    val quantity : Int = 0,
    val movement : Movement = Movement.IN,
    val date: Date = Date()
)


enum class Movement {
    IN,
    OUT,
    SOLD
}