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
import com.eutech.pawprints.home.data.CartItems
import com.eutech.pawprints.shared.presentation.utils.toPhp


@Composable
fun CartItemPrice(
    modifier: Modifier = Modifier,
    cartItems: CartItems,
) {

    val discountPercentage = cartItems.discount?.value ?: 0.0
    val discountedPrice = cartItems.price?.times((1 - discountPercentage / 100))

    Text(
        text = buildAnnotatedString {
            if (cartItems.discount != null) {
                val price = cartItems.quantity?.times(discountedPrice!!)
                withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)) {
                    append(price?.toPhp() + " ")
                }
                withStyle(style = SpanStyle(color = Color.Gray, textDecoration = TextDecoration.LineThrough)) {
                    val s = cartItems.quantity?.times(discountedPrice!!)
                    append(s?.toPhp())
                }
            } else {
                withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)) {
                    val s = cartItems.quantity?.times(discountedPrice!!)

                    append(s?.toPhp())
                }
            }
        }
    )
}