package com.eutech.pawprints.schedule.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.eutech.pawprints.doctors.data.Doctors
import com.eutech.pawprints.schedule.data.Schedule
import com.eutech.pawprints.schedule.data.display
import com.eutech.pawprints.ui.custom.Avatar


@Composable
fun ScheduleItems(
    modifier: Modifier = Modifier,
    doctors : List<Doctors> = emptyList(),
    isLoading: Boolean,
    schedule: Schedule,
    onDelete : () -> Unit,
    onAssignDoctor : (id : String ,doctorID : String) -> Unit
) {
    var deleteDialog by remember {
        mutableStateOf(false)
    }
    if (deleteDialog) {
        AlertDialog(
            onDismissRequest = { deleteDialog = false },
            title = { Text("Delete Schedule") },
            text = { Text("Are you sure you want to delete this schedule?") },
            confirmButton = {
                TextButton (onClick = {
                    deleteDialog = false
                    onDelete()
                }) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { deleteDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            "${schedule.date}".uppercase(),
            textAlign = TextAlign.Start,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold
            ),
            modifier = modifier
                .fillMaxWidth()
                .weight(1f)
        )
        val doctor = doctors.find { it.id == schedule.doctorID }
        if (doctor == null) {
            AssignDoctor(
                modifier = modifier.fillMaxWidth().weight(1f),
                doctors = doctors,
                onConfirm = {
                    onAssignDoctor(schedule.id!!,it)
                }
            )
        } else {

            Text(
                "${doctor.name}",
                textAlign = TextAlign.Start,
                modifier = modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
        }

        Text(
            "${schedule.startTime?.display()} - ${schedule.endTime?.display()}",
            textAlign = TextAlign.Start,
            modifier = modifier
                .fillMaxWidth()
                .weight(1f)
        )
        Text(
            "${schedule.slots}",
            textAlign = TextAlign.Start,
            modifier = modifier
                .fillMaxWidth()
                .weight(1f)
        )
        if (isLoading) {
            CircularProgressIndicator()
        } else {
            IconButton(
                modifier = modifier.weight(1f),
                onClick = {deleteDialog = !deleteDialog}
            ) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = "Delete"
                )
            }
        }

    }
}

@Composable
fun AssignDoctor(
    modifier: Modifier = Modifier,
    doctors : List<Doctors>,
    onConfirm : (doctorID : String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier.wrapContentSize(Alignment.TopStart)) {
        TextButton(onClick = { expanded = true }) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Localized description")
                Text("Assign")
            }

        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            doctors.forEach { doctors ->
                DropdownMenuItem(
                    text = { Text("${doctors.name}") },
                    onClick = {doctors.id?.let{
                        onConfirm(it)
                        expanded =!expanded
                    } },
                    leadingIcon = { Avatar(image = doctors.profile ?: "", size = 24.dp, onClick = {}) }
                )
            }

        }
    }
}