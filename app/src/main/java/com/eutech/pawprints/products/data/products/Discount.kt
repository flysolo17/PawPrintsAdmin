package com.eutech.pawprints.products.data.products

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class Discount(
    val value : Double = 0.00,
    val expiration : Date? = null,
) : Parcelable
