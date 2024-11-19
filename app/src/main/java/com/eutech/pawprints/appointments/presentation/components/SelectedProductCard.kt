package com.eutech.pawprints.appointments.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.material3.Card
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.eutech.pawprints.R
import com.eutech.pawprints.products.data.products.Products


@Composable
fun SelectedProductCard(
    modifier: Modifier = Modifier,
    product : Products,
    onRemove : (String) -> Unit
) {
    Card(
        modifier =  modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),

        ) {
            AsyncImage(
                modifier = modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(8.dp)),
                model = product.image, 
                contentDescription = product.name,
                contentScale = ContentScale.Crop,
                placeholder = painterResource(R.drawable.product),
                error = painterResource(R.drawable.product)
            )
            Text(
                modifier = modifier.weight(1f),
                text = product.name.toString(),
                style = MaterialTheme.typography.titleMedium
            )
            FilledIconButton(
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.onErrorContainer
                ),
                onClick = { onRemove(product.id!!) }) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete")
            }

        }
    }

}