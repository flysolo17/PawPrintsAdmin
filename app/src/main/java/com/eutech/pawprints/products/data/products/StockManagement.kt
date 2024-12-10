package com.eutech.pawprints.products.data.products

import android.graphics.Color
import android.os.Parcelable
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class StockManagement(
    val message : String = "",
    val quantity : Int = 0,
    val movement : Movement = Movement.IN,
    val date: Date = Date()
) : Parcelable


enum class Movement {
    IN,
    OUT,
    SOLD
}


@Composable
fun Movement.getColor(): androidx.compose.ui.graphics.Color {
    return when (this) {
        Movement.IN -> MaterialTheme.colorScheme.primary
        Movement.OUT -> MaterialTheme.colorScheme.error
        Movement.SOLD -> MaterialTheme.colorScheme.error
    }
}