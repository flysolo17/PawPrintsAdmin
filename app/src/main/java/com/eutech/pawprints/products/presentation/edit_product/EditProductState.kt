package com.eutech.pawprints.products.presentation.edit_product

import android.net.Uri
import com.eutech.pawprints.products.data.Category
import com.eutech.pawprints.products.data.products.Discount
import com.eutech.pawprints.products.data.products.ProductType
import com.eutech.pawprints.products.data.products.Products
import com.eutech.pawprints.products.data.products.StockManagement
import com.eutech.pawprints.shared.presentation.utils.TextFieldData
import com.eutech.pawprints.shared.presentation.utils.generateRandomNumber
import java.util.Date



data class EditProductState(
    val isLoading : Boolean = false,
    val errors : String ? = null,
    val products: Products ? = null,
    val categoryList : List<Category> = emptyList(),

    val formHasError : Boolean = false,
    //forms
    val selectedProductType : ProductType = ProductType.GOODS,

    val image : Uri ? = null,
    val name : TextFieldData = TextFieldData(),
    val desc : TextFieldData = TextFieldData(),
    val selectedCategory  : Category? = Category(),
    val features : TextFieldData = TextFieldData(),
    val contents : TextFieldData = TextFieldData(),
    val quantity : TextFieldData = TextFieldData(value = "0"),
    val cost : TextFieldData = TextFieldData(value = "0.00"),
    val price : TextFieldData = TextFieldData(value = "0.00"),
    val expiration : String = "",
    val isSaved : String ? = null,
)

