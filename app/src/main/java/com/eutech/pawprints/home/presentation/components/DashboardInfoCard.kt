package com.eutech.pawprints.home.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LockClock
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eutech.pawprints.schedule.data.countSlots
import com.eutech.pawprints.ui.theme.PawPrintsTheme


@Composable
fun DashboardInfoCard(
    modifier: Modifier = Modifier,
    icon : ImageVector,
    data : String,
    label : String,
    iconBackGround : Color,
    hasButton : Boolean = false,
    onButtonClick : () -> Unit
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(
            4.dp
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .wrapContentSize()
                    .background(
                        color = iconBackGround,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(8.dp)) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    imageVector = icon,
                    contentDescription = "Schedule",
                    tint = Color.White
                )
            }
            Spacer(
                modifier = Modifier.width(12.dp)
            )
            Column(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    label,
                    style = MaterialTheme.typography.titleSmall.copy(
                        color = Color.Gray,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    data,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }

    }
}

@Preview
@Composable
private fun DashboardCardPrev() {

    PawPrintsTheme {
        DashboardInfoCard(
            icon = Icons.Filled.LockClock,
            label = "Available Slots",
            data = "12",
            iconBackGround = Color.Blue
        ) { }
    }

}