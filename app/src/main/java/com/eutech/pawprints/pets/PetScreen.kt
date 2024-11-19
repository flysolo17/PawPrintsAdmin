package com.eutech.pawprints.pets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.eutech.pawprints.pets.components.PetCard
import com.eutech.pawprints.pets.components.PetInfo
import com.eutech.pawprints.ui.theme.PawPrintsTheme
import com.eutech.pawprints.users.UserState
import com.eutech.pawprints.users.UsersEvents
import com.eutech.pawprints.users.UsersScreen
import com.eutech.pawprints.users.components.UserCard


@Composable
fun PetScreen(
    modifier: Modifier = Modifier,
    state: PetState,
    events: (PetEvents) -> Unit,
    navController: NavController
) {
    Row(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        LazyColumn(
            modifier = modifier
                .fillMaxWidth()
                .weight(0.6f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                Text(
                    "Pets",
                    style = MaterialTheme.typography.titleLarge
                )
            }
            item {
                TextField(
                    shape = MaterialTheme.shapes.extraLarge,
                    maxLines = 1,
                    modifier = modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        errorIndicatorColor = Color.Transparent
                    ),
                    value = state.searchText,
                    onValueChange = {events(PetEvents.OnSearchPet(it))},
                    trailingIcon = {
                        if(state.searchText.isEmpty()) {
                            Icon(imageVector = Icons.Filled.Search, contentDescription = "Search")
                        } else {
                            IconButton(onClick = {events(PetEvents.OnSearchPet(""))}) {
                                Icon(imageVector = Icons.Filled.Clear, contentDescription = "Clear")
                            }
                        }

                    },
                    placeholder = { Text("Search pets...") }
                )
            }
            if (state.isLoading){
                item {
                    CircularProgressIndicator()
                }
            }
            items(state.filteredPets) {
                val isSelected = state.selectedPet?.id == it.id
                PetCard(
                    pet = it,
                    isSelected = isSelected,
                    onClick = {
                        events(PetEvents.OnSelectPet(it))
                    }
                )
            }

        }
        Box(
            modifier = modifier
                .fillMaxSize()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            if (state.selectedPet == null) {
                Text("No Pet selected")
            } else {
                PetInfo(
                    selectedPet =  state.selectedPet,
                    state = state,
                    onGetPetAppointments = {state.selectedPet.id?.let { events(PetEvents.OnGetPetAppointments(it)) }}
                )
            }

        }

    }
}




@Preview
@Composable
private fun UsersScreenPrev() {
    PawPrintsTheme {
        PetScreen(
            state = PetState(),
            events = {},
            navController = rememberNavController()
        )
    }
}