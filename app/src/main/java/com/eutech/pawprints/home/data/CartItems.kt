package com.eutech.pawprints.home.data

import com.eutech.pawprints.products.data.products.Discount


data class CartItems(
    val id : String ? = null,
    var quantity : Int ? = null,
    val discount: Discount ? = null,
    val price : Double ? = null
)

fun List<CartItems>.computeTotal(): Double {
    return this.sumOf { cartItem ->
        val price = cartItem.price ?: 0.0
        val quantity = cartItem.quantity ?: 0
        val discount = cartItem.discount

        val finalPrice = if (discount == null || discount.expiration?.time!! < System.currentTimeMillis()) {
            price
        } else {
            price - (price * discount.value / 100)
        }

        finalPrice * quantity
    }
}
