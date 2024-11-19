package com.eutech.pawprints.home.presentation.components

import androidx.compose.material.Chip
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import com.eutech.pawprints.appointments.data.appointment.Attendees


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AttendeeCardInfo(modifier: Modifier = Modifier,attendees: Attendees) {
    ListItem(
        text = { Text("${attendees.name}") },
        secondaryText = { Text("${attendees.phone}",          style = MaterialTheme.typography.labelSmall
            .copy(
                fontSize = 12.sp,
                color = Color.Gray
            )) },
        trailing = {
            Chip(onClick = {}) {
                Text("${attendees.type?.name?.lowercase()}", style = MaterialTheme.typography.labelSmall)
            }
        }
    )
}