package com.eutech.pawprints.users.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.eutech.pawprints.appointments.data.appointment.Inbox
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun InboxCard(
    modifier: Modifier = Modifier,
    inbox : Inbox
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        ListItem(
            modifier = modifier.padding(8.dp),
            text = {
                Column {
                    Text(
                        inbox.type.name,
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = Color.Gray
                        )
                    )
                    Text(
                        "${inbox.message}",
                        style = MaterialTheme.typography.titleSmall
                    )
                    FormattedDateText(inbox.createdAt)
                }

            }
        )
    }
}


@Composable
fun FormattedDateText(createdAt: Date) {
    val formattedDate = try {
        // Format the Date object to "MM/dd/yyyy hh:mm aa"
        val dateFormat = SimpleDateFormat("MM/dd/yyyy hh:mm aa", Locale.getDefault())
        dateFormat.format(createdAt)
    } catch (e: Exception) {
        "Invalid Date"
    }

    Text(
        formattedDate,
        style = MaterialTheme.typography.labelSmall.copy(
            color = Color.Gray,
            fontWeight = FontWeight.Bold
        )
    )
}
