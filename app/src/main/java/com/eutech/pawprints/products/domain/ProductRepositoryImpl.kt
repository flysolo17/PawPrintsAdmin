package com.eutech.pawprints.products.domain

import android.net.Uri
import android.util.Log
import com.eutech.pawprints.products.data.products.ProductType
import com.eutech.pawprints.products.data.products.Products
import com.eutech.pawprints.shared.presentation.utils.Results
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.toObjects
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.delay
import kotlinx.coroutines.tasks.await

class ProductRepositoryImpl(
    private val storage : FirebaseStorage,
    private val firestore : FirebaseFirestore
) : ProductRepository {
    override suspend fun createProduct(
        products: Products,
        uri: Uri?,
        results: (Results<String>) -> Unit
    ) {
        try {
            results.invoke(Results.loading("Creating Products"))
            if (uri != null) {
                val storageRef = storage.reference.child(
                    "${ProductRepository.PRODUCT_COLLECTION}/${products.id}/${uri.lastPathSegment}"
                )
                val uploadTask = storageRef.putFile(uri).await()
                val downloadUrl =storageRef.downloadUrl.await()
                products.image = downloadUrl.toString()
            }
            firestore.collection(ProductRepository.PRODUCT_COLLECTION).document(products.id!!).set(products).await()
            results.invoke(Results.success("Successfully Added!"))
        } catch (e : Exception) {
            Log.e(ProductRepository.PRODUCT_COLLECTION,e.message?: "",e)
            results.invoke(Results.failuire(e.localizedMessage ?: "Unknown error"))
        }
    }

    override suspend fun getAllProducts(results: (Results<List<Products>>) -> Unit) {
        results.invoke(Results.loading("Getting All Products"))
        firestore.collection(ProductRepository.PRODUCT_COLLECTION)
            .orderBy("createdAt",Query.Direction.DESCENDING)
            .orderBy("updatedAt",Query.Direction.DESCENDING)
            .addSnapshotListener { value, error ->
                error?.let {
                    Log.e(ProductRepository.PRODUCT_COLLECTION,it.message?:"",it)
                    results.invoke(Results.failuire(it.localizedMessage?: "Unknown error"))
                }
                value?.let {
                    results.invoke(Results.success(it.toObjects(Products::class.java)))
                }
            }
    }

    override suspend fun getProductByType(
        type: ProductType,
        results: (Results<List<Products>>) -> Unit,
    ) {
        results.invoke(Results.loading("Getting Products where type == ${type}"))
        val products = firestore.collection(ProductRepository.PRODUCT_COLLECTION)
            .whereEqualTo("type",type)
            .get()
            .await()
            .toObjects<Products>()
        results.invoke(Results.success(products))
    }

    override suspend fun getProductByID(
        productID : String,
        results: (Results<Products?>) -> Unit
    ) {
        results.invoke(Results.loading("Getting  Product by id $productID"))
        delay(1000L)
        firestore.collection(ProductRepository.PRODUCT_COLLECTION)
            .document(productID)
            .addSnapshotListener { value, error ->
                error?.let {
                    Log.e(ProductRepository.PRODUCT_COLLECTION,it.message?:"",it)
                    results.invoke(Results.failuire(it.localizedMessage?: "Unknown error"))
                }
                value?.let {
                    val product = it.toObject(Products::class.java)
                    results.invoke(Results.success(product))
                }
            }
    }

    override suspend fun deleteProduct(id: String, results: (Results<String>) -> Unit) {
        results.invoke(Results.loading("Deleting product...."))
        firestore.collection(ProductRepository.PRODUCT_COLLECTION)
            .document(id)
            .delete()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    results.invoke(Results.success("Successfully Deleted!"))
                } else {
                    results.invoke(Results.failuire("Failed to delete"))
                }
            }.addOnFailureListener {
                results.invoke(Results.failuire(it.message.toString()))
            }
    }

}