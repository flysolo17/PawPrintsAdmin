package com.eutech.pawprints.appointments.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Group
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

import com.eutech.pawprints.appointments.data.appointment.Attendees
import com.eutech.pawprints.shared.presentation.utils.TextFieldData
import com.eutech.pawprints.shared.presentation.utils.generateRandownString
import com.eutech.pawprints.ui.custom.PrimaryTextField


@Composable
fun AttendeeDialog(
    modifier: Modifier = Modifier,

    onAddedAttendee : (Attendees) -> Unit
) {
    var dialog by remember {
        mutableStateOf(false)
    }
    var name by remember {
        mutableStateOf(TextFieldData())
    }
    var email  by remember {
        mutableStateOf(TextFieldData())
    }
    var phone by remember {
        mutableStateOf(TextFieldData())
    }

    if (dialog) {
        AlertDialog(
            dismissButton = {
                TextButton(onClick = { dialog = !dialog }) {
                  Text(text = "Cancel")
               }
            },
            title = {
                    Text(
                        text = "Add Attendee",
                        style = MaterialTheme.typography.titleLarge
                    )
            },
            onDismissRequest = {
                               dialog = !dialog
            },
            confirmButton = {
                TextButton(onClick = {
                    onAddedAttendee(
                        Attendees(
                            id = generateRandownString(20),
                            name = name.value,
                            phone = phone.value,
                            email = email.value
                        )
                    )
                    dialog = !dialog
                }) {
                    Text(text = "Add")
                }
                
            },
            text = {
                Column(
                    modifier = modifier
                        .width(600.dp)
                        .padding(12.dp)
                        .verticalScroll(rememberScrollState()),
                ) {
                    PrimaryTextField(textField = name, label = "Fullname") {
                        val newName = it
                        var errorMessage: String? = null
                        var hasError = false

                        if (newName.isEmpty()) {
                            errorMessage = "Name cannot be empty"
                            hasError = true
                        } else if (!newName.all { it.isLetter() || it.isWhitespace() }) {
                            errorMessage = "Name can only contain letters and spaces"
                            hasError = true
                        }
                        name = name.copy(
                            value = newName,
                            hasError, errorMessage
                        )

                    }
                    PrimaryTextField(textField = email, label = "Email") {
                        val newEmail = it
                        var errorMessage: String? = null
                        var hasError = false

                        if (newEmail.isEmpty()) {
                            errorMessage = "Email cannot be empty"
                            hasError = true
                        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(newEmail).matches()) {
                            errorMessage = "Invalid email address"
                            hasError = true
                        }
                        email = email.copy(
                            value = newEmail,
                            errorMessage = errorMessage,
                            hasError = hasError
                        )
                    }
                    PrimaryTextField(textField = phone, label = "Phone"
                    ) {
                        val phones = it
                        var errorMessage: String? = null
                        var hasError = false

                        if (phones.isEmpty()) {
                            errorMessage = "Phone number cannot be empty"
                            hasError = true
                        } else if (!phones.startsWith("09")) {
                            errorMessage = "Phone number must start with 09"
                            hasError = true
                        } else if (phones.length != 11) {
                            errorMessage = "Phone number must be 11 characters long"
                            hasError = true
                        } else if (!phones.all { it.isDigit() }) {
                            errorMessage = "Phone number can only contain digits"
                            hasError = true
                        }
                        phone = phone.copy(
                            value = phones,
                            errorMessage = errorMessage,
                            hasError = hasError
                        )
                    }
                }
            }
        )
    }

    FilledIconButton(
        shape = RoundedCornerShape(4.dp),
        onClick = { dialog = true }
    ) {
        Icon(imageVector = Icons.Rounded.Add, contentDescription =  "Add Attendee")
    }


}