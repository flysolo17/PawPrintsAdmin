package com.eutech.pawprints.shared.presentation.utils

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp


@Composable
fun ErrorScreen(
    modifier: Modifier = Modifier,
    @DrawableRes image : Int ? = null,
    title : String = "Not Found",
    content : @Composable () -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (image != null) {
            Image(
                painter = painterResource(id = image),
                contentDescription = title
            )
        }
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            modifier = modifier.padding(16.dp))
        content()
    }

}