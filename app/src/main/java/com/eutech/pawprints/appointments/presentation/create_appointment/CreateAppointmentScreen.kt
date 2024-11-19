package com.eutech.pawprints.appointments.presentation.create_appointment


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items

import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons

import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.window.Popup
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.eutech.pawprints.R
import com.eutech.pawprints.appointments.data.appointment.Attendees

import com.eutech.pawprints.appointments.presentation.components.AttendeeDialog
import com.eutech.pawprints.appointments.presentation.components.PawPrintTimePicker
import com.eutech.pawprints.appointments.presentation.components.ProductDialog
import com.eutech.pawprints.appointments.presentation.components.ProductItemCard
import com.eutech.pawprints.appointments.presentation.components.SelectedProductCard
import com.eutech.pawprints.doctors.data.DoctorWithSchedules
import com.eutech.pawprints.doctors.data.Doctors
import com.eutech.pawprints.shared.presentation.utils.toExpireFormat
import com.eutech.pawprints.shared.presentation.utils.toast

import com.eutech.pawprints.ui.theme.PawPrintsTheme
import kotlinx.coroutines.delay

import java.util.Calendar
@Composable
fun CreateAppointmentScreen(
    modifier: Modifier = Modifier,
    state : CreateAppointmentState,
    events : (CreateAppointmentEvents) -> Unit,
    navHostController: NavHostController
) {
    val context = LocalContext.current
    LaunchedEffect(key1 = state) {
        if (state.errors != null) {
            context.toast(state.errors)
        }
        if (state.created != null) {
            context.toast(state.created)
            delay(1000)
            navHostController.popBackStack()
        }
    }
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    onClick = { navHostController.popBackStack() }
                ) {
                    Icon(imageVector = Icons.Rounded.ArrowBack, contentDescription = "Back")
                }
                Text(text = "Create Appointment", style = MaterialTheme.typography.titleLarge)
            }
            Button(
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                onClick = {
                    events(CreateAppointmentEvents.OnCreateAppointment)
                },
                enabled = !state.isLoading,

            ) {
                Row(
                    modifier = modifier.padding(4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (state.isLoading) {
                        CircularProgressIndicator(modifier = modifier.size(24.dp))
                    } else {
                        Icon(
                            imageVector = Icons.Default.Save, contentDescription = "Save")
                    }
                    Text(text = "Save")
                }
            }
        }
        AppointmentForm(
            modifier = modifier
                .fillMaxWidth(),
            state = state,
            events = events
        )


    }

}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppointmentForm(
    modifier: Modifier,
    state : CreateAppointmentState,
    events: (CreateAppointmentEvents) -> Unit
) {


    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        TextField(
            modifier = modifier
                .fillMaxWidth()
                .padding(8.dp),
            value = state.title,
            onValueChange = {events(CreateAppointmentEvents.OnTitleChange(it))},
            label = { Text("Purpose") },
        )
        Row(
            modifier = modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            DatePickerTextField(
                modifier = modifier.weight(1f),
                selectedDate = state.date,

            ) {
                events(CreateAppointmentEvents.OnDateChange(it))
            }
            PawPrintTimePicker(
                modifier = modifier.weight(.5f),
                hours = state.startTime,
                label = "Start Time",
                onConfirm = {
                    events.invoke(CreateAppointmentEvents.OnStartTimeChange(it.hour,it.minute))
                }
            )
            PawPrintTimePicker(
                modifier = modifier.weight(.5f),
                hours = state.endTime,
                label = "End Time",
                onConfirm = {
                    events.invoke(CreateAppointmentEvents.OnEndTimeChange(it.hour,it.minute))
                }
            )
        }

        TextField(
            value = state.note,
            onValueChange = {events(CreateAppointmentEvents.OnNoteChange(it))},
            minLines = 3,
            label = {
                Text(text = "Notes (Optional)")
            },
            modifier = modifier
                .fillMaxWidth()
                .padding(8.dp)
        )
        Text(
            modifier = modifier.padding(8.dp),
            text = "Select a doctor",
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleSmall
        )
        LazyRow(
            modifier = modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            val doctors = state.doctors.map { it.doctors!! }
            items(doctors) { doctor ->
                val isSelected = state.attendees.any { it.id==doctor.id }
                DoctorAppointmentCard(
                    it = doctor,
                    isSelected = isSelected,
                    onSelect = {events.invoke(CreateAppointmentEvents.OnAddDoctorAttendee(doctor))}
                )
            }
        }

        Row(
            modifier = modifier.fillMaxWidth()
        ) {
            Column(
                modifier = modifier
                    .weight(1f)
                    .padding(8.dp)
            ) {
                    Row(
                        modifier = modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Attendees")
                        AttendeeDialog {
                            events.invoke(CreateAppointmentEvents.OnAttendeeAdded(it))
                        }
                    }
                state.attendees.forEachIndexed { index, attendees ->
                    AttendeeCard(it =attendees, onRemove = {events.invoke(CreateAppointmentEvents.OnRemoveAttendee(index))})
                }
            }
        }

    }
}

@Composable
fun DoctorAppointmentCard(
    modifier: Modifier = Modifier,
    it: Doctors,
    isSelected : Boolean,
    onSelect: () -> Unit
) {
    Column(
        modifier = modifier
            .width(150.dp)
            .padding(8.dp)
            .border(
                width = 2.dp,
                color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Gray,
                shape = RoundedCornerShape(8.dp)
            ),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            modifier = modifier
                .size(80.dp)
                .clip(CircleShape)
                .clickable {
                    onSelect()
                }
            ,
            model = it.profile,
            contentDescription = "${it.name}",
            error = painterResource(id = R.drawable.doctor),
            placeholder = painterResource(id = R.drawable.doctor)
        )
        Spacer(modifier = modifier.height(8.dp))
        Text(
            text = it.name ?: "",
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun AttendeeCard(
    modifier: Modifier = Modifier,
    it: Attendees,
    onRemove : () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Text(
                    text = it.name.toString() + " (${it.type?.name})",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Email : ${it.email}",
                    color = Color.Gray
                )
                Text(
                    text = "Phone : ${it.phone}",
                    color = Color.Gray
                )
            }
            IconButton(onClick =  onRemove) {
                Icon(imageVector = Icons.Rounded.Delete, contentDescription = "Remove")
            }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerTextField(
    modifier: Modifier = Modifier,
    selectedDate: String,
    onChange: (String) -> Unit
) {
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let{
                        val formattedDate = convertMillisToDate(it)
                        onChange(formattedDate)
                    }
                    showDatePicker = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    // TextField with Calendar Icon
    TextField(
        modifier = modifier.fillMaxWidth(),
        value = selectedDate,
        onValueChange = {},
        readOnly = true,
        trailingIcon = {
            IconButton(onClick = { showDatePicker = true }) {
                Icon(imageVector = Icons.Default.CalendarMonth, contentDescription = "Calendar")
            }
        },
        label = { Text("Date") },
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDocked() {
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    val selectedDate = datePickerState.selectedDateMillis?.let {
        convertMillisToDate(it)
    } ?: ""

    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = selectedDate,
            onValueChange = { },
            label = { Text("DOB") },
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { showDatePicker = !showDatePicker }) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Select date"
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
        )

        if (showDatePicker) {
            Popup(
                onDismissRequest = { showDatePicker = false },
                alignment = Alignment.TopStart
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(y = 64.dp)
                        .shadow(elevation = 4.dp)
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(16.dp)
                ) {
                    DatePicker(
                        state = datePickerState,
                        showModeToggle = false
                    )
                }
            }
        }
    }
}

fun convertMillisToDate(millis: Long): String {
    val calendar = Calendar.getInstance().apply {
        timeInMillis = millis
    }
    val date = calendar.time
    return date.toExpireFormat()
}



@Preview(
    showBackground = true,

)
@Composable
private fun CreateAppointmentPreview() {
    PawPrintsTheme {
        CreateAppointmentScreen(
            state = CreateAppointmentState(),
            events = {},
            navHostController = rememberNavController()
        )
    }
}