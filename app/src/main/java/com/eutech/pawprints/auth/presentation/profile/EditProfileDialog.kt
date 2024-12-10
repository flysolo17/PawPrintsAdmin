package com.eutech.pawprints.auth.presentation.profile


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.eutech.pawprints.auth.data.Administrator
import com.eutech.pawprints.shared.presentation.utils.TextFieldData
import com.eutech.pawprints.ui.custom.PrimaryTextField


//val name : String ? = "",
//val phone : String ? = "",
//val email : String ? = null,
//
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileDialog(
    modifier: Modifier = Modifier,
    admin: Administrator,
    onDismiss: () -> Unit,
    onConfirm: (
        name: String,
        phone: String,
        email: String
    ) -> Unit
) {
    var name by remember { mutableStateOf(TextFieldData(value = admin.name ?: "")) }
    var phone by remember { mutableStateOf(TextFieldData(value = admin.phone ?: "")) }
    var email by remember { mutableStateOf(TextFieldData(value = admin.email ?: "")) }

    var formHasError by remember { mutableStateOf(false) }

    AlertDialog(
        modifier = modifier,
        onDismissRequest = onDismiss,
        title = { Text("Edit Profile") },
        text = {
            Column {
                com.eutech.pawprints.ui.custom.PrimaryTextField(
                    textField = name,
                    label = "Enter Name",
                    onValueChange = {
                        val hasError = it.isEmpty()
                        name = name.copy(value = it, hasError = hasError, errorMessage = if (hasError) "Name is required" else null)
                        formHasError = hasError
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
                com.eutech.pawprints.ui.custom.PrimaryTextField(
                    textField = phone,
                    label = "Enter Phone Number",
                    onValueChange = {
                        val hasError = it.isEmpty() || !it.all { char -> char.isDigit() }
                        phone = phone.copy(value = it, hasError = hasError, errorMessage = if (hasError) "Valid phone number is required" else null)
                        formHasError = hasError
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
                com.eutech.pawprints.ui.custom.PrimaryTextField(
                    textField = email,
                    readOnly = true,
                    label = "Enter Email Address",
                    onValueChange = {
                        val hasError = it.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(it).matches()
                        email = email.copy(value = it, hasError = hasError, errorMessage = if (hasError) "Valid email address is required" else null)
                        formHasError = hasError
                    }
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (!formHasError) {
                        onConfirm(name.value, phone.value, email.value)
                    }
                },
                enabled = !formHasError
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
