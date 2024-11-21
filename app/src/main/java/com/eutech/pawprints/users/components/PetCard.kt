package com.eutech.pawprints.users.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.eutech.pawprints.shared.data.pets.Pet
import com.eutech.pawprints.shared.data.pets.getAge

@Composable
fun PetCard(
    modifier: Modifier = Modifier,
    pet: Pet,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(150.dp)
            .clickable(onClick = onClick)
            .clip(RoundedCornerShape(8.dp))
    ) {
        AsyncImage(
            model = pet.image,
            contentDescription = "${pet.name} Image",
            contentScale = ContentScale.Crop,
            modifier = modifier.fillMaxSize()
        )


        Box(
            modifier = modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f)) // Dark overlay
        )


        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(12.dp)
        ) {

            Text(
                text = pet.name ?: "Unknown", // Use the getAge function
                style = MaterialTheme.typography.titleMedium.copy(
                    color = Color.White.copy(alpha = 0.8f),
                    fontWeight = FontWeight.Bold
                )
            )
            Text(
                text = pet.birthday?.getAge() ?: "Age Unknown", // Use the getAge function
                style = MaterialTheme.typography.bodySmall.copy(
                    color = Color.White.copy(alpha = 0.8f)
                )
            )
        }
    }
}
