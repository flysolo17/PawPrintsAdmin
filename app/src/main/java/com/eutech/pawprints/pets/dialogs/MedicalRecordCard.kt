package com.eutech.pawprints.pets.dialogs

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardElevation
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.rememberImagePainter
import com.eutech.pawprints.shared.data.medical.MedicalRecordWithDoctor
import com.eutech.pawprints.shared.data.medical.Prescription
import com.eutech.pawprints.shared.presentation.utils.displayDate

@Composable
fun MedicalRecordCard(data: MedicalRecordWithDoctor) {
    val record = data.record
    val doctor  = data.doctors
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = MaterialTheme.shapes.medium,
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (record.images.isNotEmpty()) {
                Text(text = "Images:", style = MaterialTheme.typography.titleMedium)
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(record.images) { imageUri ->
                        ImageCard(imageUri)
                    }
                }
            }
            Text(text = "Diagnosis: ${record.diagnosis}", style = MaterialTheme.typography.titleMedium)
            Text(text = "Treatment: ${record.treatment}", style = MaterialTheme.typography.titleMedium)
            Text(text = "Vet Name: ${record.doctorID ?: "Unknown"}", style = MaterialTheme.typography.titleMedium)
            Text(text = "Notes: ${record.notes}", style = MaterialTheme.typography.titleMedium)
            Text(text = "Date: ${record.date.displayDate()}", style = MaterialTheme.typography.titleMedium)

            Text(text = "Prescriptions:", style = MaterialTheme.typography.titleMedium)
            record.prescriptions.forEach { prescription ->
                PrescriptionCard(prescription)
            }


        }
    }
}

@Composable
fun PrescriptionCard(prescription: Prescription) {
    Column(
        modifier = Modifier.padding(8.dp)
    ) {
        Text(text = "Medication Name: ${prescription.medicationName}", style = MaterialTheme.typography.labelMedium)
        Text(text = "Dosage: ${prescription.dosage}", style = MaterialTheme.typography.labelMedium)
        Text(text = "Duration: ${prescription.duration}", style = MaterialTheme.typography.labelMedium)
        Text(text = "Instructions: ${prescription.instructions}", style = MaterialTheme.typography.labelMedium)
        Text(text = "Notes: ${prescription.notes}", style = MaterialTheme.typography.labelMedium)
        Text(text = "Issued Date: ${prescription.issuedDate.displayDate()}", style = MaterialTheme.typography.labelMedium)
        Text(text = "Expiration Date: ${prescription.expirationDate?.displayDate()}", style = MaterialTheme.typography.labelMedium)
    }
}

@Composable
fun ImageCard(imageUri: String) {
    AsyncImage(
        model = imageUri,
        contentDescription = "Medical Image",
        contentScale = ContentScale.Crop,
        modifier = Modifier.size(100.dp).clip(MaterialTheme.shapes.small)
    )
}
