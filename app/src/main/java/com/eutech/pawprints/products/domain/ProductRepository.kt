package com.eutech.pawprints.products.domain

import android.net.Uri
import com.eutech.pawprints.products.data.products.ProductType
import com.eutech.pawprints.products.data.products.Products
import com.eutech.pawprints.shared.presentation.utils.Results

interface ProductRepository {
    suspend fun createProduct(products: Products, uri : Uri?, results: (Results<String>) -> Unit)
    suspend fun getAllProducts(results: (Results<List<Products>>) -> Unit)
    suspend fun getProductByType(type : ProductType, results: (Results<List<Products>>) -> Unit)
    suspend fun getProductByID(
        productID : String,
        results: (Results<Products?>
    ) -> Unit)




    suspend fun deleteProduct(id : String,results: (Results<String>) -> Unit)
    companion object {
        const val PRODUCT_COLLECTION = "products"
    }
}