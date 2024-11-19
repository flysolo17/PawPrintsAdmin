package com.eutech.pawprints.products.domain

import com.eutech.pawprints.products.data.Category
import com.eutech.pawprints.shared.presentation.utils.Results

interface CategoryRepository {
    suspend fun createCategory(category: Category,result : (Results<String>) -> Unit)
    suspend fun getAllCategory(result: (Results<List<Category>>) -> Unit)
    suspend fun deleteCategory(id : String,result: (Results<String>) -> Unit)


}