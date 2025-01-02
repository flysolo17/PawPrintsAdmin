package com.eutech.pawprints.pets.components

import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.eutech.pawprints.doctors.data.Doctors
import com.eutech.pawprints.pets.dialogs.AddMedicalRecordDialog
import com.eutech.pawprints.pets.dialogs.MedicalRecordCard
import com.eutech.pawprints.shared.data.medical.MedicalRecord
import com.eutech.pawprints.shared.data.medical.MedicalRecordWithDoctor
import com.eutech.pawprints.shared.data.pets.Pet


@Composable
fun MedicalInfo(
    modifier: Modifier = Modifier,
    pet: Pet,
    isLoading: Boolean,
    doctors: List<Doctors>,
    records : List<MedicalRecordWithDoctor>,
    onSaveMedicalRecord: (MedicalRecord,List<Uri>) -> Unit
) {
    var isDialogVisible by remember { mutableStateOf(false) }

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        LazyColumn(
            contentPadding = PaddingValues(8.dp),
            modifier = Modifier.fillMaxSize()
        ) {

            items(records) { recordWithDoctor ->
                MedicalRecordCard(recordWithDoctor)
            }
        }

        FloatingActionButton(
            onClick = { isDialogVisible = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.MedicalServices,
                contentDescription = "Add Medical Record"
            )
        }

        if (isDialogVisible) {
            AddMedicalRecordDialog(
                doctors = doctors,
                onDismiss = { isDialogVisible = false },
                pet = pet,
                onSave = { medicalRecord,images ->
                    onSaveMedicalRecord(medicalRecord,images)
                    isDialogVisible = false
                }
            )
        }
    }
}