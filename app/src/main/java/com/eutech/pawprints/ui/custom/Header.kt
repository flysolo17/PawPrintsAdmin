package com.eutech.pawprints.ui.custom

import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import okhttp3.internal.http2.Header


@Composable
fun Header(
    modifier: Modifier = Modifier,
    title : String
) {
    Text(
        title,
        style = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.SemiBold
        )
    )
}

@Composable
fun SubHeader(
    modifier: Modifier = Modifier,
    subtitle : String,
    ) {
    Text(
        subtitle,
        style = MaterialTheme.typography.titleSmall.copy(
            color = Color.Gray
        )
    )
}

@Composable
fun Label(
    modifier: Modifier = Modifier,
    label : String ,
) {
    Text(
        label,
        style = MaterialTheme.typography.labelSmall.copy(
            color = Color.Gray
        )
    )
}