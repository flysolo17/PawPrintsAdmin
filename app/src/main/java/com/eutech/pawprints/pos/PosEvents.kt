package com.eutech.pawprints.pos

import com.eutech.pawprints.orders.OrderEvents
import com.eutech.pawprints.products.data.products.Products
import com.eutech.pawprints.products.presentation.product.ProductEvents
import com.eutech.pawprints.shared.data.transactions.Transaction
import com.eutech.pawprints.shared.presentation.routes.MainRouter


sealed interface PosEvents  {
    data object OnGetProducts : PosEvents
    data object OnGetAllCategories : PosEvents
    data class OnSelectProduct(val product : Products) : PosEvents
    data class OnRemoveItem(val index : Int) : PosEvents
    data class OnCategoryClick(val index : Int) : PosEvents
    data class IncreaseQuantity(val index: Int) : PosEvents
    data class DecreaseQuantity(val index: Int) : PosEvents
    data object ComputeTotal : PosEvents


    data class OnAmountReceivedChange(
        val text  : String
    ) : PosEvents
    data object Submit: PosEvents
    data class SaveTransaction(val transaction : Transaction): PosEvents
}