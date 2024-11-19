package com.eutech.pawprints.schedule.presentation
import android.app.DatePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavHostController
import com.eutech.pawprints.schedule.data.Hours
import com.eutech.pawprints.schedule.data.Schedule
import com.eutech.pawprints.schedule.data.display
import com.eutech.pawprints.shared.presentation.components.PawPrintDatePicker
import com.eutech.pawprints.shared.presentation.components.PawPrintTimePicker
import com.eutech.pawprints.shared.presentation.utils.generateRandomNumber
import com.eutech.pawprints.shared.presentation.utils.toast
import java.util.Date


@Composable
fun ScheduleScreen(
    modifier: Modifier = Modifier,
    state : ScheduleState,
    events: (ScheduleEvents) -> Unit,
    navHostController: NavHostController
) {
    var showDialog by remember {
        mutableStateOf(false)
    }
    val context = LocalContext.current
    LaunchedEffect(state) {
        if (state.errors != null) {
            context.toast(state.errors)
        }
        if (state.created != null) {
            context.toast(state.created)
        }
    }

    LazyColumn(
        modifier = modifier.padding(16.dp)
    ) {
        item {
            ScheduleHeader(
                isLoading= state.isCreatingSchedule,
                onCreate = {
                    showDialog = !showDialog
                }
            )
        }
        item {
            TableHeader()
        }
        items(state.schedules) {
            ScheduleItems(
                isLoading = state.isDeleting == it.id,
                doctors = state.doctors,
                schedule = it,
                onDelete = {
                    events(ScheduleEvents.OnDelete(it.id?:"")) },
                onAssignDoctor = {id ,doctorID ->
                    events(ScheduleEvents.OnAssignDoctor(id = id, doctorID = doctorID))
                }
            )
        }
    }
    if (showDialog) {
        CreateScheduleDialog(
            doctors = state.doctors,
            onDismissRequest = {
                showDialog = !showDialog
            },
            onScheduleCreate = {
                events(ScheduleEvents.OnCreateSchedule(schedule = it))
                showDialog = !showDialog
            }
        )
    }
}

@Composable
fun TableHeader(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.primary
            )
            .padding(8.dp),
    ) {
        Text(
            "Date".uppercase(),
            style = MaterialTheme.typography.titleMedium.copy(color = Color.White),
            textAlign = TextAlign.Start,
            modifier = modifier
                .fillMaxWidth()
                .weight(1f)
        )
        VerticalDivider()
        Text(
            "Doctor".uppercase(),
            textAlign = TextAlign.Start,
            style = MaterialTheme.typography.titleMedium.copy(color = Color.White),
            modifier = modifier
                .fillMaxWidth()
                .weight(1f)
        )
        VerticalDivider()
        Text(
            "Time".uppercase(),
            textAlign = TextAlign.Start,
            style = MaterialTheme.typography.titleMedium.copy(color = Color.White),
            modifier = modifier
                .fillMaxWidth()
                .weight(1f)
        )
        VerticalDivider()
        Text(
            "Slots".uppercase(),
            textAlign = TextAlign.Start,
            style = MaterialTheme.typography.titleMedium.copy(color = Color.White),
            modifier = modifier
                .fillMaxWidth()
                .weight(1f)
        )
        VerticalDivider()
        Text(
            "Actions".uppercase(),
            textAlign = TextAlign.Start,
            style = MaterialTheme.typography.titleMedium.copy(color = Color.White),
            modifier = modifier
                .fillMaxWidth()
                .weight(1f)
        )
    }
}

@Composable
fun ScheduleHeader(
    isLoading : Boolean,
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
        Text("Schedules", style = MaterialTheme.typography.titleLarge)
        Button (
            enabled = !isLoading,
            shape = MaterialTheme.shapes.small,
            onClick = {onCreate()}
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = modifier.size(16.dp))
                } else {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Add"
                    )
                }
                Text(if(isLoading) {"Creating..."} else "New")
            }
        }
    }
}


