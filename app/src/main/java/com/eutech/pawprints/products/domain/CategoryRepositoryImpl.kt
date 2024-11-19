package com.eutech.pawprints.products.domain

import com.eutech.pawprints.products.data.Category
import com.eutech.pawprints.shared.presentation.utils.Results
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage

class CategoryRepositoryImpl(
    private val firestore: FirebaseFirestore,

) : CategoryRepository {
    override suspend fun createCategory(category: Category, result: (Results<String>) -> Unit) {
        result.invoke(Results.loading("Adding Category"))
        firestore.collection(CATEGORY_COLLECTION)
            .document(category.id!!)
            .set(category)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    result.invoke(Results.success("Successfully Added!"))
                } else {
                    result.invoke(Results.failuire("Failed adding category"))
                }
            }.addOnFailureListener {
                result.invoke(Results.failuire(it.localizedMessage ?: "Failed adding category"))
            }
    }

    override suspend fun getAllCategory(result: (Results<List<Category>>) -> Unit) {
        firestore
            .collection(CATEGORY_COLLECTION)
            .orderBy("createdAt",Query.Direction.ASCENDING)
            .addSnapshotListener { value, error ->
                error?.let {
                    result.invoke(Results.failuire(it.localizedMessage ?: "Unknown Error"))
                }
                value?.let {
                    val categoryList = it.toObjects(Category::class.java)
                    result.invoke(Results.success(categoryList))
                }
            }
    }

    override suspend fun deleteCategory(id : String, result: (Results<String>) -> Unit) {
        result.invoke(Results.loading("Delete Category"))
        firestore.collection(CATEGORY_COLLECTION)
            .document(id)
            .delete()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    result.invoke(Results.success("Successfully Deleted!"))
                } else {
                    result.invoke(Results.failuire("Failed deleting category"))
                }
            }.addOnFailureListener {
                result.invoke(Results.failuire(it.localizedMessage ?: "Failed deleting category"))
            }
    }
    companion object {
        const val CATEGORY_COLLECTION = "categories"
    }
}