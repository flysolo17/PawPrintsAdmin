package com.eutech.pawprints.products.presentation.edit_product

import android.net.Uri
import android.printservice.PrintDocument
import com.eutech.pawprints.products.data.Category
import com.eutech.pawprints.products.data.products.ProductType
import com.eutech.pawprints.products.data.products.Products
import com.eutech.pawprints.products.presentation.create_product.CreateProductEvents
import com.eutech.pawprints.products.presentation.create_product.FormField
import com.eutech.pawprints.products.presentation.view_product.ViewProductEvents


sealed interface EditProductEvents  {
    data class OnImageChanged(val image : Uri) : EditProductEvents
    data class OnProductTypeChange(val type : ProductType) : EditProductEvents
    data class OnTextFieldChange(val text : String,val form : FormField) : EditProductEvents
    data class OnSelectCategory(val category: Category?) : EditProductEvents
    data object OnGetAllCategories : EditProductEvents

    data class OnSetProduct(val products: Products) : EditProductEvents
    data class  OnSave(
        val oldProduct : Products
    ): EditProductEvents

    data class OnExpirationSave(val date : String) : EditProductEvents

    data class OnGetProductByID(val productID : String) : EditProductEvents
    data class OnDeleteCategory(
        val id : String
    ) : EditProductEvents
}

enum class FormField {
    NAME,
    DESC,
    FEATURES,
    CONTENTS,
    QUANTITY,
    COST,
    PRICE,
}