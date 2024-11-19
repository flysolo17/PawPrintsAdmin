package com.eutech.pawprints.ui.custom

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.eutech.pawprints.ui.theme.PawPrintsTheme


@Composable
fun PawPrintActionButton(
    modifier: Modifier = Modifier,
    icon : ImageVector,
    title : String,
    onClick : () -> Unit,
    colors: ButtonColors = ButtonDefaults.buttonColors()
) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(4.dp),
        colors = colors
    ) {
        Row(
            modifier = modifier.padding(2.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = icon, contentDescription = "button icon")
            Text(text = title)
        }
    }

}

@Preview
@Composable
private fun PawPrintActionButtonPrev() {
    PawPrintsTheme {
        PawPrintActionButton(
            icon = Icons.Default.Delete,
            title = "Delete",
            onClick = {},
            colors = ButtonDefaults.success()
        )
    }
}


@Composable
fun ButtonDefaults.danger(): ButtonColors {
    return buttonColors(
        containerColor = MaterialTheme.colorScheme.errorContainer,
        contentColor = MaterialTheme.colorScheme.onErrorContainer
    )
}

@Composable
fun ButtonDefaults.success(): ButtonColors {
    return buttonColors(
        containerColor = Color(0xFF4C662B), // Green color
        contentColor = Color.White // White color
    )
}

