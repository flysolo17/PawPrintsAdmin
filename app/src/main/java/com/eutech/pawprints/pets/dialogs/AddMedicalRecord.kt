package com.eutech.pawprints.pets.dialogs

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.rememberAsyncImagePainter
import com.eutech.pawprints.doctors.data.Doctors
import com.eutech.pawprints.shared.data.medical.MedicalRecord
import com.eutech.pawprints.shared.data.medical.Prescription
import com.eutech.pawprints.shared.data.pets.Pet
import com.eutech.pawprints.shared.presentation.components.PawPrintDatePicker
import java.util.Date


@Composable
fun AddMedicalRecordDialog(
    pet: Pet,
    doctors: List<Doctors>,
    onDismiss: () -> Unit,
    onSave: (MedicalRecord,List<Uri>) -> Unit
) {
    var diagnosis by remember { mutableStateOf("") }
    var treatment by remember { mutableStateOf("") }
    var selectedDoctor by remember { mutableStateOf<Doctors?>(null) }

    var notes by remember { mutableStateOf("") }
    var prescriptions by remember { mutableStateOf(listOf<Prescription>()) }
    var prescriptionDialogVisible by remember { mutableStateOf(false) }
    var date by remember { mutableStateOf(Date()) }
    val selectedImages = remember { mutableStateListOf<Uri>() }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris ->
        selectedImages.addAll(uris)
    }

    if (prescriptionDialogVisible) {
        AddPrescriptionDialog(
            onDismiss = { prescriptionDialogVisible = false },
            onAdd = { prescription ->
                prescriptions = prescriptions + prescription
                prescriptionDialogVisible = false
            }
        )
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Add Medical Record",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    items(selectedImages) { uri ->
                        Card(
                            modifier = Modifier
                                .size(100.dp)
                                .padding(4.dp)
                        ) {
                            Box {
                                Image(
                                    painter = rememberAsyncImagePainter(uri),
                                    contentDescription = "Selected Image",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                                IconButton(
                                    onClick = { selectedImages.remove(uri) },
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .background(Color.Black.copy(alpha = 0.5f))
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Delete Image",
                                        tint = Color.White
                                    )
                                }
                            }
                        }
                    }
                    item {
                        FilledIconButton(
                            shape = MaterialTheme.shapes.medium,
                            onClick = { imagePickerLauncher.launch("image/*") },
                            modifier =Modifier.size(100.dp)) {
                            Icon(imageVector = Icons.Default.AddPhotoAlternate, contentDescription = "Add Image")
                        }
                    }
                }
                // Input Fields
                OutlinedTextField(
                    value = diagnosis,
                    onValueChange = { diagnosis = it },
                    label = { Text("Diagnosis") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = treatment,
                    onValueChange = { treatment = it },
                    label = { Text("Treatment") },
                    modifier = Modifier.fillMaxWidth()
                )



                Spacer(modifier = Modifier.height(8.dp))




                DoctorDropdownMenu(
                    doctors = doctors,
                    selectedDoctor = selectedDoctor,
                    onDoctorSelected = { doctor ->
                        selectedDoctor = doctor
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Notes") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Prescription List
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Prescriptions",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(onClick = { prescriptionDialogVisible = true }) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = "Add Prescription")
                    }
                }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(top = 8.dp)
                ) {
                    items(prescriptions) { prescription ->
                        Text("- ${prescription.medicationName} (${prescription.dosage})", style = MaterialTheme.typography.bodyMedium)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Actions
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            onSave(
                                MedicalRecord(
                                    petID = pet.id ?: "",
                                    diagnosis = diagnosis,
                                    treatment = treatment,
                                    doctorID = selectedDoctor?.id,
                                    notes = notes,
                                    prescriptions = prescriptions,
                                    date = date
                                ),
                                selectedImages
                            )
                        }
                    ) {
                        Text("Save")
                    }
                }
            }
        }
    }
}

@Composable
fun AddPrescriptionDialog(
    onDismiss: () -> Unit,
    onAdd: (Prescription) -> Unit
) {
    var medicationName by remember { mutableStateOf("") }
    var dosage by remember { mutableStateOf("") }
    var duration by remember { mutableStateOf("") }
    var instructions by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var issuedDate by remember { mutableStateOf(Date()) }
    var expirationDate by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.background,
            modifier = Modifier.padding(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Add Prescription",
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = medicationName,
                    onValueChange = { medicationName = it },
                    label = { Text("Medication Name") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = dosage,
                    onValueChange = { dosage = it },
                    label = { Text("Dosage") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = duration,
                    onValueChange = { duration = it },
                    label = { Text("Duration") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = instructions,
                    onValueChange = { instructions = it },
                    label = { Text("Instructions") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                PawPrintDatePicker(
                    label = "Expiration Date",
                    value = expirationDate,

                    onChange = {
                        expirationDate = it
                    },
                    modifier = Modifier.fillMaxWidth()
                )


                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            val expiration = if (expirationDate.isNotEmpty()) {
                                Date(expirationDate)
                            } else {
                             null
                            }

                            onAdd(
                                Prescription(
                                    medicationName = medicationName,
                                    dosage = dosage,
                                    duration = duration,
                                    instructions = instructions,
                                    notes = notes,
                                    issuedDate = issuedDate,
                                    expirationDate = expiration
                                )
                            )
                        }
                    ) {
                        Text("Add")
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoctorDropdownMenu(
    doctors: List<Doctors>, // Assuming `Doctor` is a data class with `name` and `contact` fields
    selectedDoctor: Doctors?,
    onDoctorSelected: (Doctors) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val dropdownLabel = selectedDoctor?.name ?: "Select Doctor"

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it }
    ) {


        OutlinedTextField(
            value = dropdownLabel,
            onValueChange = {}, // Text field is read-only
            readOnly = true,
            label = { Text("Select Doctor") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.fillMaxWidth().menuAnchor(),
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            doctors.forEach { doctor ->
                DropdownMenuItem(
                    onClick = {
                        onDoctorSelected(doctor)
                        expanded = false
                    },
                    text = { Text(text = "${doctor.name} (${doctor.phone})") }
                )
            }
        }
    }
}
