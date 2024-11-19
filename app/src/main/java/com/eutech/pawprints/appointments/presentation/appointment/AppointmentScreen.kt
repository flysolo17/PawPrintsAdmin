package com.eutech.pawprints.appointments.presentation.appointment

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

import com.eutech.pawprints.appointments.data.appointment.AppointmentStatus
import com.eutech.pawprints.appointments.data.appointment.Appointments
import com.eutech.pawprints.schedule.data.display
import com.eutech.pawprints.shared.presentation.routes.MainRouter
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)

@Composable
fun AppointmentScreen(
    modifier: Modifier = Modifier,
    state : AppointmentState,
    events : (AppointmentEvent) -> Unit,
    navHostController: NavHostController
) {
    LazyVerticalGrid(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(8.dp)
    ) {
        item(
            span = { GridItemSpan(2) }
        ) {
            AppointmentTopBar(onCreate = {
                navHostController.navigate(MainRouter.CreateAppointment.route)
            })
        }
        val items = AppointmentStatus.entries.toList()
        item(
            span = { GridItemSpan(2) }
        ) {
            Row(modifier = modifier
                .background(
                    color = Color.LightGray,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(8.dp)

            ) {
                items.forEach {
                    val isSelected = state.selectedTab == it
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = modifier
                            .padding(4.dp)
                            .background(
                                color = if (isSelected) MaterialTheme.colorScheme.secondary else Color.Transparent,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .clickable {
                                events(AppointmentEvent.OnTabSelected(it))
                            }
                    ) {
                        Text(
                            modifier = modifier.padding(8.dp),
                            text = it.name,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) MaterialTheme.colorScheme.onSecondary else Color.Gray
                        )
                    }
                }
            }
        }

        val filteredAppointments = state.appointments.filter { it.status?.name == state.selectedTab.name }
        items(filteredAppointments,key={it.id!!}) {
            AppointmentCard(it = it, events = events)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppointmentCard(
    modifier: Modifier = Modifier,
    it: Appointments,
    events: (AppointmentEvent) -> Unit
) {
    OutlinedCard(
    ) {
        Row(
            modifier = modifier.padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            DisplayDate(scheduleDate = it.scheduleDate ?: "")
            Spacer(modifier = modifier.width(8.dp))
            Column(
                modifier = modifier
                    .weight(1f)
            ) {
                Text(
                    text = it.title?.uppercase() ?:"",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = modifier.height(8.dp))
                val date = buildAnnotatedString {

                    withStyle(style = SpanStyle(color = Color.Gray, fontWeight = FontWeight.Normal)) {
                        append("Date : ")
                    }
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("${it.scheduleDate}")
                    }
                }

                Text(
                    text = date,
                    style = MaterialTheme.typography.titleSmall
                )
                val annotatedString = buildAnnotatedString {
                    withStyle(style = SpanStyle(color = Color.Gray, fontWeight = FontWeight.Normal)) {
                        append("Time : ")
                    }
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("${it.startTime?.display()} - ${it.endTime?.display()}")
                    }
                }

                Text(
                    text = annotatedString,
                    style = MaterialTheme.typography.titleSmall
                )
                Spacer(modifier = modifier.height(8.dp))
                Text(text = "Attendees",
                    style = MaterialTheme.typography.titleSmall,
                    color = Color.Gray,
                    fontWeight = FontWeight.Bold
                )
                it.attendees.forEach {
                    Column(
                        modifier = modifier.padding(2.dp)
                    ) {
                        Text(text = "${it.name} (${it.type})",
                            fontWeight = FontWeight.Bold
                        )
                        Text(text = it.email?:"")
                        Text(text = "${it.phone}")
                    }
                }

                if (!it.note.isNullOrEmpty()) {
                    Text(
                        text = "Note : ${it.note}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.error
                    )
                }

            }
            val context = LocalContext.current
            EditDropDown(onSave = { s ->
                events.invoke(AppointmentEvent.OnUpdateStatus(
                    id = it.id!!,
                    status = s,
                    context = context
                ))
            })
        }
    }
}
@Composable
fun EditDropDown(
    modifier: Modifier = Modifier,
    onSave: (AppointmentStatus) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedStatus by remember { mutableStateOf(AppointmentStatus.PENDING) } // Default value

    val statusOptions = AppointmentStatus.entries.toTypedArray()

    Column {
        OutlinedButton(
            modifier = modifier,
            onClick = { expanded = true },
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(text = "Edit")
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                onClick = {
                    expanded = false
                    onSave(AppointmentStatus.COMPLETED)
                }
            ) {
                Text(text = "Appointment Complete")
            }
            DropdownMenuItem(
                onClick = {
                    expanded = false
                    onSave(AppointmentStatus.CANCELLED)
                }
            ) {
                Text(text = "Cancel Appointment")
            }
            val context = LocalContext.current
            DropdownMenuItem(
                onClick = {
                    Toast.makeText(context, "Not yet implemented", Toast.LENGTH_SHORT).show()
                }
            ) {
                Text(text = "Reschedule")
            }
            DropdownMenuItem(
                onClick = {
                    Toast.makeText(context, "Not yet implemented", Toast.LENGTH_SHORT).show()
                }
            ) {
                Text(text = "Add Attendees")
            }
        }
    }
}


@Composable
fun DisplayDate(scheduleDate: String) {
   if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
       val formatter =   DateTimeFormatter.ofPattern("MMM, dd yyyy")
       val date = LocalDate.parse(scheduleDate, formatter)

       val day = date.dayOfMonth.toString()
       val month = date.month.getDisplayName(java.time.format.TextStyle.SHORT, Locale.ENGLISH)
       val annotatedString = buildAnnotatedString {
           withStyle(style = SpanStyle(
               color = MaterialTheme.colorScheme.primary,
               fontSize = 18.sp,
               fontWeight = FontWeight.Black,

               )) {
               append(day)
           }
           append("\n")
           withStyle(
               style = SpanStyle(
                   fontWeight = FontWeight.Black,
               )) {
               append(month)
           }

       }

       Text(
           text = annotatedString,
           textAlign = TextAlign.Center
       )
    } else {
        Text("Invalid Date")
    }

}
@Composable
fun AppointmentTopBar(
    modifier: Modifier = Modifier,
    onCreate : () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
        ) {
            Text(text = "Appointments", style = MaterialTheme.typography.titleLarge)
        }
        Row {
            Button(
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                onClick = onCreate
            ) {
                Row(
                    modifier = modifier.padding(4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
                    Text(text = "Create Appointment")
                }
            }
        }
    }
}