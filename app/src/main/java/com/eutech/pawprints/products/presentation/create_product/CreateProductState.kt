package com.eutech.pawprints.products.presentation.create_product

import android.net.Uri
import com.eutech.pawprints.products.data.Category
import com.eutech.pawprints.products.data.products.ProductType
import com.eutech.pawprints.shared.presentation.utils.TextFieldData


data class CreateProductState(
    val isLoading : Boolean = false,
    val message : String ? = null,
    val formHasError : Boolean = false,
    val errors : String ? = null,



    val categoryList : List<Category> = emptyList(),
    //forms
    val selectedProductType : ProductType = ProductType.GOODS,

    val image : Uri ? = null,
    val name : TextFieldData = TextFieldData(),
    val desc : TextFieldData = TextFieldData(),
    val selectedCategory  : Category ? = Category(),
    val features : TextFieldData = TextFieldData(),
    val contents : TextFieldData = TextFieldData(),
    val quantity : TextFieldData = TextFieldData(value = "0"),
    val cost : TextFieldData = TextFieldData(value = "0.00"),
    val price : TextFieldData = TextFieldData(value = "0.00"),

    val created : String ? = null,
)
