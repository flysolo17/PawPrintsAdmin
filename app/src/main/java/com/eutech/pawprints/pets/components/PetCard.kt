package com.eutech.pawprints.pets.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.eutech.pawprints.R
import com.eutech.pawprints.shared.data.pets.Pet
import com.eutech.pawprints.shared.data.users.Users
import com.eutech.pawprints.ui.theme.PawPrintsTheme


@Composable
fun PetCard(
    modifier: Modifier = Modifier,
    pet: Pet,
    isSelected : Boolean,
    onClick : () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = if (isSelected) MaterialTheme.colorScheme.primary.copy(
                    alpha = 0.1f
                ) else MaterialTheme.colorScheme.surface,
                shape = MaterialTheme.shapes.medium
            )
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        AsyncImage(
            model = pet.image,
            contentScale = ContentScale.Crop,
            contentDescription = "${pet.name} profile",
            error = painterResource(R.drawable.profile_bold),
            placeholder = painterResource(R.drawable.profile_bold),
            modifier = modifier
                .size(36.dp)
                .background(
                    color = Color.Gray,
                    shape = CircleShape
                )
                .clip(CircleShape)
        )
        Column {
            Text(
                "${pet.name}",
                style = MaterialTheme.typography.titleMedium.copy(
                    color =  MaterialTheme.colorScheme.onSurface
                )
            )
            Text("${pet.species}",
                style = MaterialTheme.typography.labelSmall
                    .copy(
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
            )

        }
    }
}


@Composable
fun PetCardInfo(modifier: Modifier = Modifier,pet: Pet) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        AsyncImage(
            model = pet.image,
            contentScale = ContentScale.Crop,
            contentDescription = "${pet.name} profile",
            error = painterResource(R.drawable.profile_bold),
            placeholder = painterResource(R.drawable.profile_bold),
            modifier = modifier
                .size(54.dp)
                .background(
                    color = Color.Gray,
                    shape = CircleShape
                )
                .clip(CircleShape)
        )
        Column {
            Text(
                "${pet.name}",
                style = MaterialTheme.typography.titleMedium.copy(
                    color =  MaterialTheme.colorScheme.onSurface
                )
            )
            Text("${pet.species}",
                style = MaterialTheme.typography.labelSmall
                    .copy(
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
            )

        }
    }
}
@Preview
@Composable
private fun PetCardPrev() {
    PawPrintsTheme {
        PetCard(
            pet = Pet(
                id = "1234",
                name = "Juan Dela Cruz",
                species = "Dog",
                image = "https://upload.wikimedia.org/wikipedia/commons/7/7c/Profile_avatar_placeholder_large.png"
            ),
            isSelected = true,
            onClick = {}
        )
    }

}