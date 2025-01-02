package com.eutech.pawprints.pets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

import com.eutech.pawprints.shared.presentation.components.SearchText
import com.eutech.pawprints.shared.presentation.routes.MainRouter
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
    LazyVerticalGrid(
        modifier = modifier.fillMaxSize().padding(16.dp),
        columns = GridCells.Fixed(5),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item(
            span = { GridItemSpan(5) }
        ) {
            Column {
                Row(
                    modifier = modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Pets", style = MaterialTheme.typography.titleLarge)
                    SearchText(
                        modifier = modifier.width(400.dp),
                        label = "Search pet",
                        value = state.searchText
                    ) { events(PetEvents.OnSearchPet(it)) }
                }


                val speciesList = listOf("all") + state.pets.mapNotNull { it.species?.lowercase()?.trim() }.distinct()

                ScrollableTabRow(
                    selectedTabIndex = speciesList.indexOf(state.selectedSpecies),
                    modifier = Modifier.fillMaxWidth(),
                    contentColor = MaterialTheme.colorScheme.primary
                ) {
                    speciesList.forEachIndexed { index, species ->
                        Tab(
                            selected = state.selectedSpecies == species,
                            onClick = {
                                events(PetEvents.OnSelectSpecies(species))
                            },
                            text = {
                                Text(
                                    text =if (species.isEmpty()){
                                        "unknown"
                                    } else species.replaceFirstChar { it.uppercase() },
                                    color = if (state.selectedSpecies == species) MaterialTheme.colorScheme.primary else Color.Gray
                                )
                            }
                        )
                    }
                }



            }
        }

        items(state.filteredPets) { pet ->
            com.eutech.pawprints.users.components.PetCard(pet = pet) {
                navController.navigate(MainRouter.VIEW_PETS.createRoute(pet.id ?: ""))
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