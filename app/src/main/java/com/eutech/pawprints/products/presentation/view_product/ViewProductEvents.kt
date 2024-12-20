package com.eutech.pawprints.products.presentation.view_product

import android.content.Context
import androidx.navigation.NavHostController
import java.util.Date

sealed interface ViewProductEvents  {
    data class OnGetProductByID(val productID : String) : ViewProductEvents

    data class OnDeleteProduct(
        val id : String,
        val context : Context,
        val navHostController: NavHostController) : ViewProductEvents


    data class OnAddStocks(
        val id : String,
        val quantity : Int,
        val date : Date?
    ) : ViewProductEvents
}