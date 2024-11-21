package com.eutech.pawprints.users.tabs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import com.eutech.pawprints.pets.PetState
import com.eutech.pawprints.shared.data.pets.Pet
import com.eutech.pawprints.shared.presentation.utils.ErrorScreen
import com.eutech.pawprints.users.PetTabState
import com.eutech.pawprints.users.components.PetCard


@Composable
fun PetsTab(
    modifier: Modifier = Modifier,
    petTabState: PetTabState
) {
    LazyVerticalGrid(
        modifier = modifier.fillMaxSize(),
        columns = GridCells.Adaptive(
            minSize = 150.dp
        ),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (petTabState.isLoading) {
            item(
                span = { GridItemSpan(maxLineSpan) }
            ) {
                LinearProgressIndicator(
                    modifier = modifier.fillMaxWidth()
                )
            }
        }
        if(petTabState.errors != null) {
            item {
                Box(
                    modifier = modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    ErrorScreen(
                        title = petTabState.errors
                    ) {
                    }
                }
            }
        }
        if (petTabState.errors == null && petTabState.pets.isEmpty() &&
            !petTabState.isLoading) {
            item {
                Box(
                    modifier = modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("no pets yet!")
                }
            }
        }
        items(petTabState.pets) {
            PetCard(pet=it) {

            }
        }
    }
}