package com.eutech.pawprints.schedule.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.eutech.pawprints.doctors.data.Doctors
import com.eutech.pawprints.schedule.data.Hours
import com.eutech.pawprints.schedule.data.Schedule
import com.eutech.pawprints.schedule.data.display
import com.eutech.pawprints.shared.presentation.components.PawPrintDatePicker
import com.eutech.pawprints.shared.presentation.components.PawPrintTimePicker
import com.eutech.pawprints.shared.presentation.utils.generateRandomNumber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateScheduleDialog(
    modifier: Modifier = Modifier,
    doctors: List<Doctors>,
    onDismissRequest: () -> Unit,
    onScheduleCreate: (Schedule) -> Unit
) {
    var doctorID by remember { mutableStateOf("") }
    var date by remember { mutableStateOf<String>("") }
    var startTime by remember { mutableStateOf<Hours?>(null) }
    var endTime by remember { mutableStateOf<Hours?>(null) }
    var slots by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            dismissOnBackPress  = false,
            dismissOnClickOutside = false
        )
    ) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface,
            modifier = modifier
                .width(900.dp)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Column(
                modifier = modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(text = "Create Schedule", style = MaterialTheme.typography.titleLarge)
                ExposedDropdownMenuBox(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(4.dp),
                    expanded = expanded,
                    onExpandedChange =  { expanded = !expanded }
                ) {
                    val selectedDoctor = doctors.find { it.id == doctorID }
                    OutlinedTextField(
                        value = selectedDoctor?.name ?: "Select Doctor",
                        onValueChange = { },
                        label = { Text("Doctor") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        readOnly = true,
                        trailingIcon = {
                            Icon(
                                imageVector = if (expanded) Icons.Filled.ArrowDropUp else Icons.Filled.ArrowDropDown,
                                contentDescription = "Doctor List"
                            )
                        }
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier.exposedDropdownSize()
                    ) {
                        doctors.forEach { type ->
                            DropdownMenuItem(
                                text = { Text(type.name ?: "unknown doctor") },
                                onClick = {
                                    doctorID = type.id ?: ""
                                    expanded = false
                                }
                            )
                        }
                    }
                }
                PawPrintDatePicker(
                    label = "Select Date",
                    value = date,

                    onChange = {
                        date = it
                    }
                )
                Row(
                    modifier = modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    PawPrintTimePicker(
                        modifier =modifier.weight(1f),
                        label = "Start Time",

                        onChange = {
                            startTime = it
                        },
                        value = startTime?.display() ?: ""
                    )
                    PawPrintTimePicker(
                        modifier =modifier.weight(1f),
                        label = "End Time",
                        onChange = {
                            endTime = it
                        },
                        value = endTime?.display() ?: ""
                    )
                }
                OutlinedTextField(
                    value = slots,
                    onValueChange = { slots = it },
                    label = { Text("Slots") },
                    modifier = modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    supportingText = {
                        Text("How many reservations do you want to cater at this time frame? ")
                    }
                )
                Row(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = MaterialTheme.colorScheme.onErrorContainer
                        ),
                        onClick = { onDismissRequest() })
                    {
                        Text("Cancel")
                    }
                    Button(
                        shape = MaterialTheme.shapes.small,
                        onClick = {
                            val schedule = Schedule(
                                id = generateRandomNumber(12),
                                date = date,
                                doctorID = doctorID,
                                startTime = startTime,
                                endTime = endTime,
                                slots = slots.toIntOrNull()
                            )
                            onScheduleCreate(
                                schedule
                            )
                        }
                    ) {
                        Text("Confirm")
                    }
                }
            }
        }
    }
}
