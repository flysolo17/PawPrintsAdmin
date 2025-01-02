package com.eutech.pawprints.products.presentation.create_product

import android.content.Context
import android.net.Uri
import androidx.navigation.NavHostController
import com.eutech.pawprints.products.data.Category
import com.eutech.pawprints.products.data.products.ProductType


sealed interface CreateProductEvents {
    data class OnImageChanged(val image : Uri) : CreateProductEvents
    data class OnProductTypeChange(val type : ProductType) : CreateProductEvents
    data class OnTextFieldChange(val text : String,val form : FormField) : CreateProductEvents
    data class OnSelectCategory(val category: Category ?) : CreateProductEvents
    data object OnGetAllCategories : CreateProductEvents
    data class OnCreateCategory(val category : String) : CreateProductEvents
    data class SubmitProduct(val context: Context,val navHostController: NavHostController): CreateProductEvents
    data class DeleteCategory(
        val id : String,

    ) : CreateProductEvents
}

enum class FormField {
    NAME,
    DESC,
    FEATURES,
    CONTENTS,
    QUANTITY,
    COST,
    PRICE
}