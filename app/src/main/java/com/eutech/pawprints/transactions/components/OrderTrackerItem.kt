package com.eutech.pawprints.transactions.components

import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eutech.pawprints.appointments.data.appointment.Inbox
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale


@Composable
fun OrderTrackerItem(
    modifier: Modifier = Modifier,
    inbox: Inbox
) {
    Row(
        modifier = modifier.fillMaxWidth().height(60.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ){
        InboxDate(inbox.createdAt)
        Column(
            modifier = modifier.height(60.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = modifier.size(8.dp).background(color = Color.Gray, shape = CircleShape)
            ) {
            }
            Box(
                modifier = modifier.width(1.dp).fillMaxHeight().background(color = Color.Gray)
            ) {
            }
        }

        Text("${inbox.message}", style = MaterialTheme.typography.labelLarge.copy(
            color = Color.Gray
        ))
    }
}



@Composable
fun InboxDate(date: Date) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

        val localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
        val day = localDate.dayOfMonth.toString()
        val month = localDate.month.getDisplayName(java.time.format.TextStyle.SHORT, Locale.ENGLISH)

        val annotatedString = buildAnnotatedString {
            withStyle(
                style = SpanStyle(
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                )
            ) {
                append(day)
            }
            append("\n")
            withStyle(
                style = SpanStyle(
                    fontWeight = FontWeight.Bold,
                )
            ) {
                append(month)
            }
        }

        // Display the styled text
        Text(
            text = annotatedString,
            textAlign = TextAlign.Center
        )
    } else {
        Text("Invalid Date")
    }
}
