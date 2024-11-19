package com.eutech.pawprints.pets.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.eutech.pawprints.R
import com.eutech.pawprints.shared.data.pets.Pet
import com.eutech.pawprints.shared.data.pets.getAge
import com.eutech.pawprints.shared.presentation.components.DetailRow
import com.eutech.pawprints.ui.theme.PawPrintsTheme
import java.util.Date

@Composable
fun PetDetails(
    modifier: Modifier = Modifier,
    pet: Pet
) {
    val age = pet.birthday?.getAge()
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Pet Image
        AsyncImage(
            model = pet.image,
            contentDescription = "${pet.name} image",
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Pet Name and Age
        Text(
            text = pet.name ?: "Unknown Name",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = age?.let { "$it years old" } ?: "Age not available",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Pet Details",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        DetailRow(label = "Breed", value = pet.breed ?: "Unknown")
        DetailRow(label = "Sex", value = pet.otherDetails?.get("Sex") ?: "Unknown")
        val age = pet.birthday?.getAge()?.toString() ?: "Unknown"
        DetailRow(label = "Date of Birth", value = "${pet.birthday ?: "Unknown"} ($age)")

        Spacer(modifier = Modifier.height(16.dp))


        PetHealthDetails(pet.otherDetails)
    }
}

@Composable
fun PetHealthDetails(otherDetails: Map<String, String>?) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(
            text = "Other Details",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(8.dp))

        otherDetails?.forEach { (key, value) ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = key,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = value,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        } ?: run {
            Text(
                text = "No health details available.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        PetActionsCard(title = "Medical Record", icon = R.drawable.medical_record)
        PetActionsCard(title = "Prescriptions", icon = R.drawable.prescriptions)
    }
}
@Preview(showBackground = true)
@Composable
private fun PetDetailsPrev(

) {
    val samplePet = Pet(
        id = "P001",
        ownerID = "U001",
        name = "Buddy",
        image = "https://example.com/images/buddy.jpg",
        species = "Dog",
        breed = "Golden Retriever",
        birthday = "2020-06-15",
        otherDetails = mapOf(
            "Weight" to "30.5 kg", // in kilograms
            "Height" to "60.0 cm", // in centimeters
            "BMI" to "16.95", // Body Mass Index
            "Medical Conditions" to "Allergy to pollen, Hip Dysplasia"
        ),
        createdAt = Date(),
        updatedAt = Date()
    )
    PawPrintsTheme {
        PetDetails(
            pet = samplePet
        )
    }
}