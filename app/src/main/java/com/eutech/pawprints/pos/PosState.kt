package com.eutech.pawprints.pos

import com.eutech.pawprints.products.data.Category
import com.eutech.pawprints.products.data.products.Products
import com.eutech.pawprints.shared.data.transactions.Transaction
import com.eutech.pawprints.shared.data.transactions.TransactionItems


data class PosState(
    val isLoading : Boolean = false,
    val selectedCategoryIndex : Int = 0,
    val products: List<Products> = emptyList(),
    val categoryList : List<Category> = emptyList(),
    val errors : String ? = null,
    val items : List<TransactionItems> = emptyList(),
    val total : Double = 0.00,
    val showPaymentDialog : Boolean  = false,

    val amountReceived : String = "",
    val isSubmitting : Boolean = false,
    val isSubmitted : String? = null
 )