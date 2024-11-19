package com.eutech.pawprints.ui.custom

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import com.eutech.pawprints.products.data.products.Products
import com.eutech.pawprints.shared.presentation.utils.toPhp


@Composable
fun PriceText(
    modifier: Modifier = Modifier,
    product : Products
) {
    val discountPercentage = product.discount?.value ?: 0.0
    val discountedPrice = product.price * (1 - discountPercentage / 100)

    Text(
        text = buildAnnotatedString {
            if (product.discount != null) {
                withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)) {
                    append(discountedPrice.toPhp() + " ")
                }
                withStyle(style = SpanStyle(color = Color.Gray, textDecoration = TextDecoration.LineThrough)) {
                    append(product.price.toPhp())
                }
            } else {
                withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)) {
                    append(product.price.toPhp())
                }
            }
        }
    )


}