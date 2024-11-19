package com.eutech.pawprints.products.data.products

import com.eutech.pawprints.products.data.Category
import com.eutech.pawprints.shared.presentation.utils.generateRandomNumber
import java.util.Date

data class Products(
    val id : String ? = generateRandomNumber(),
    val name : String ? = null,
    var image : String ? = null,
    val type : ProductType = ProductType.GOODS,
    val description : String ? = null,
    val categoryID : String ? = null,
    val features : String ?= null,
    val contents : String ?  = null,
    val quantity : Int = 0,
    val visibility : Boolean = false,
    val cost : Double = 0.00,
    val price : Double = 0.00,
    val discount : Discount? = null,
    val expiration : Date ? = null,
    val createdAt : Date = Date(),
    val updatedAt : Date = Date(),
    val stocks : List<StockManagement> = emptyList()
)


data class Category(
    val id : String ? = generateRandomNumber(8),
    val name : String ? = null,
    val createdAt : Date ? = Date()
)
enum class ProductType {
    GOODS,
    SERVICES
}