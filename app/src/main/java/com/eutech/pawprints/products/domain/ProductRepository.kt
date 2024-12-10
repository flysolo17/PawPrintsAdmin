package com.eutech.pawprints.products.domain

import android.net.Uri
import com.eutech.pawprints.products.data.products.ProductType
import com.eutech.pawprints.products.data.products.Products
import com.eutech.pawprints.shared.data.transactions.TransactionItems
import com.eutech.pawprints.shared.presentation.utils.Results
import java.util.Date

interface ProductRepository {
    suspend fun createProduct(products: Products, uri : Uri?, results: (Results<String>) -> Unit)
    suspend fun getAllProducts(results: (Results<List<Products>>) -> Unit)
    suspend fun getProductByType(type : ProductType, results: (Results<List<Products>>) -> Unit)
    suspend fun getProductByID(
        productID : String,
        results: (Results<Products?>
    ) -> Unit )

    suspend fun addProduct(
        id : String,
        quantity : Int,
        expiry : Date? ,
        results: (Results<String>) -> Unit
    )
    suspend fun deleteProduct(id : String,results: (Results<String>) -> Unit)
    suspend fun updateProductsQuantity(item : List<TransactionItems>)
    companion object {
        const val PRODUCT_COLLECTION = "products"
    }
}