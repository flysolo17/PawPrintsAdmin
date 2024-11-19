package com.eutech.pawprints.ui.custom

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.eutech.pawprints.ui.theme.PawPrintsTheme


@Composable
fun PrimaryButton(
    modifier: Modifier = Modifier,
    label : String,
    isLoading : Boolean = false,
    enabled : Boolean = true,
    onClick : () -> Unit
) {
    Button(onClick = onClick,
        shape = RoundedCornerShape(8.dp),
        enabled = enabled,
        modifier = modifier
            .padding(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(text = label)
        }
        
    }
}


@Preview(showBackground = true)
@Composable
private fun PrimaryButtonPrev() {
    PawPrintsTheme {

        PrimaryButton(label = "Login") {
        }
    }
}