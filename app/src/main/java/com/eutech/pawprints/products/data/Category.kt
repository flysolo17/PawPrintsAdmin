package com.eutech.pawprints.products.data

import androidx.compose.ui.graphics.drawscope.Stroke
import com.eutech.pawprints.shared.presentation.utils.generateRandomNumber
import java.util.Date

data class Category(
    val id : String ? = generateRandomNumber(8),
    val name : String ? = null,
    val createdAt : Date ? = Date()
)
