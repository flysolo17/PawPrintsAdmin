package com.eutech.pawprints.home.presentation.components

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material.ExperimentalMaterialApi

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.eutech.pawprints.appointments.data.appointment.AppointmentStatus
import com.eutech.pawprints.appointments.data.appointment.AppointmentWithAttendeesAndPets
import com.eutech.pawprints.appointments.data.appointment.Appointments
import com.eutech.pawprints.appointments.data.appointment.displayTime
import com.eutech.pawprints.appointments.data.appointment.getColor
import com.eutech.pawprints.appointments.data.appointment.getIcon
import com.eutech.pawprints.appointments.presentation.appointment.DisplayDate

import com.eutech.pawprints.schedule.data.display
import com.eutech.pawprints.shared.presentation.components.DetailRow
import com.eutech.pawprints.shared.presentation.routes.MainRouter
import com.eutech.pawprints.ui.custom.GroupAvatars
import com.eutech.pawprints.ui.custom.Header
import com.eutech.pawprints.ui.custom.SubHeader


@Composable
fun AppointmentCard(
    modifier: Modifier = Modifier,
    data: AppointmentWithAttendeesAndPets,
    isLoading : Boolean,
    onUpdateStatus : (AppointmentStatus,Appointments) -> Unit
) {
    val appointment= data.appointments
    val pets = data.pets
    val doctor = data.doctors
    val users = data.users
    val borderColor = appointment.status.getColor() // Get color based on status
    var isAppointmentClicked by remember {
        mutableStateOf(false)
    }
    if (isAppointmentClicked) {
        AppointmentInfoDialog(data =data ,
            isLoading = isLoading,
            onDismiss = { isAppointmentClicked = !isAppointmentClicked }
            , onConfirm = { s,a->
            onUpdateStatus(s,a)
        })
    }

    Card(
        shape = MaterialTheme.shapes.medium,
        modifier = modifier.fillMaxWidth().clickable {
            isAppointmentClicked = !isAppointmentClicked
        },
        border = BorderStroke(width = 2.dp, color = borderColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = borderColor.copy(alpha = 0.1f)
                )
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            DisplayDate(appointment.scheduleDate ?: "")
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .weight(1f)
            ){
                Text(
                    text = appointment.title ?: "",
                    style = MaterialTheme.typography.titleMedium
                )

                SubHeader(
                    subtitle = appointment.displayTime()
                )
                val attendees = listOf(
                    doctor?.profile ?: "",
                    users?.profile ?: ""
                ) + pets.map { it.image!! }
                GroupAvatars(
                    avatars = attendees
                )
            }

            if (isLoading) {
                CircularProgressIndicator(
                    modifier = modifier.size(
                        24.dp
                    )
                )
            } else {
                Icon(
                    imageVector = Icons.Default.ArrowForwardIos,
                    contentDescription = "Arrow"
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun AppointmentInfoDialog(
    modifier: Modifier = Modifier,
    isLoading : Boolean,
    data: AppointmentWithAttendeesAndPets,
    onDismiss : () -> Unit,
    onConfirm : (AppointmentStatus,Appointments) -> Unit
) {

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = modifier
                .fillMaxWidth(0.8f) // 80% of the screen width
                .wrapContentHeight()
                .padding(16.dp),
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.background,

        ) {
            LazyVerticalGrid(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(4.dp)
            ) {
                item(span = { GridItemSpan(2) }) {
                    Text(
                        "${data.appointments.title}",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = modifier.fillMaxWidth().padding(bottom = 12.dp)
                    )
                }
                item(span = { GridItemSpan(1) }) {
                    AppointmentInfo(title = "Appointment Details") {
                        DetailRow(
                            label = "Location",
                            value = "Aucena Veterinary Clinic",
                            hasDivider = false
                        )
                        DetailRow(
                            label = "Date",
                            value = "${data.appointments.scheduleDate}",
                            hasDivider = false
                        )

                        DetailRow(
                            label = "Time",
                            value = "${data.appointments.startTime?.display()} - ${data.appointments.endTime?.display()}",
                            hasDivider = false
                        )
                        DetailRow(
                            label = "Status",
                            value = "${data.appointments.status?.name}",
                            hasDivider = false
                        )
                    }
                }
                data.users?.let {
                    item(span = { GridItemSpan(1) }) {
                        AppointmentInfo(title = "Created By") {
                            UserCardInfo(users = data.users)
                        }
                    }
                }
                item(span = { GridItemSpan(2) }) {
                    HorizontalDivider()
                }
                item(span = { GridItemSpan(1) }) {
                    AppointmentInfo(title = "Attendees") {
                        Column(
                            modifier = modifier.fillMaxWidth().padding(8.dp)
                        ) {
                            data.appointments.attendees.forEach {
                                AttendeeCardInfo(attendees = it)
                            }
                        }
                    }
                }
                item(span = { GridItemSpan(1) }) {
                    AppointmentInfo(title = "Pets") {
                        Column(
                            modifier = modifier.fillMaxWidth().padding(8.dp)
                        ) {
                            data.pets.forEach {
                                PetAppointmentCard(pet = it)
                            }
                        }
                    }
                }
                item(span = { GridItemSpan(2) }) {
                    Row(
                        modifier = modifier.fillMaxWidth().padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = {onDismiss()}) {
                            Text("Close")
                        }
                        Spacer(
                            modifier = modifier.width(8.dp)
                        )
                        val current = data.appointments.status?.name
                        EditStatusButton(){
                            onConfirm(it,data.appointments)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EditStatusButton(
    modifier: Modifier = Modifier,
    onConfirm: (AppointmentStatus) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val statuses = AppointmentStatus.entries.toTypedArray()
    Box(modifier = Modifier.width(200.dp).wrapContentSize(Alignment.TopStart)) {
        OutlinedButton(
            onClick = { expanded = !expanded },
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Edit Status")
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            statuses.forEach { status ->
                DropdownMenuItem(
                    leadingIcon = {
                        val icon = status.getIcon()
                        val color = status.getColor()
                        Box(
                            modifier = modifier.wrapContentSize().background(
                                color = color,
                                shape = CircleShape
                            )
                        ){
                            Icon(
                                imageVector = icon,
                                contentDescription = status.name,
                                modifier = modifier.size(24.dp).padding(4.dp),
                                tint = Color.White
                            )
                        }
                    },
                    text = { Text(status.name) },
                    onClick = {
                        onConfirm(status)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun AppointmentInfo(
    modifier: Modifier = Modifier,
    title : String,
    content: @Composable () -> Unit
) {
    Column {
        Text("${title.uppercase()}", style = MaterialTheme.typography.labelSmall.copy(
            color = Color.Gray,
        ))
        content()
    }
}