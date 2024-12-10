package com.eutech.pawprints.products.data.products

import android.os.Parcelable
import com.eutech.pawprints.products.data.Category
import com.eutech.pawprints.shared.data.transactions.TransactionItems
import com.eutech.pawprints.shared.presentation.utils.generateRandomNumber
import kotlinx.parcelize.Parcelize
import java.io.Serializable
import java.util.Date


@Parcelize
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
) : Parcelable


data class Category(
    val id : String ? = generateRandomNumber(8),
    val name : String ? = null,
    val createdAt : Date ? = Date()
)
enum class ProductType {
    GOODS,
    SERVICES
}


fun Products.toItem() : TransactionItems {
    return  TransactionItems(
        productID = this.id,
        name = this.name,
        imageUrl = this.image,
        quantity = 1,
        price = this.price
    )
}