package com.eutech.pawprints.home.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.eutech.pawprints.R
import com.eutech.pawprints.shared.data.pets.Pet


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PetAppointmentCard(
    modifier: Modifier = Modifier,
    pet: Pet
) {
    ListItem(
      icon = {
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
      } ,
        text = { Text(
            "${pet.name}",
            style = MaterialTheme.typography.titleMedium.copy(
                color =  MaterialTheme.colorScheme.onSurface
            ))
        },
        secondaryText = {
            Text("${pet.species}",
                style = MaterialTheme.typography.labelSmall
                    .copy(
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
            )
        },
    )
}