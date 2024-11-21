package com.eutech.pawprints.users

import android.graphics.drawable.Icon
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
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.eutech.pawprints.pets.PetEvents
import com.eutech.pawprints.pets.components.PetInfo
import com.eutech.pawprints.ui.theme.PawPrintsTheme
import com.eutech.pawprints.users.components.UserCard
import com.eutech.pawprints.users.components.UserInfoLayout


@Composable
fun UsersScreen(
    modifier: Modifier = Modifier,
    state: UserState,
    events: (UsersEvents) -> Unit,
    navController: NavController
) {
    Row(
        modifier = modifier.fillMaxSize().padding(16.dp)
    ) {
        LazyColumn(
            modifier = modifier.fillMaxWidth().weight(0.4f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                Text(
                    "Users",
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
                    onValueChange = {events(UsersEvents.OnSearch(it))},
                    trailingIcon = {
                        if(state.searchText.isEmpty()) {
                            Icon(imageVector = Icons.Filled.Search, contentDescription = "Search")
                        } else {
                            IconButton(onClick = {events(UsersEvents.OnSearch(""))}) {
                                Icon(imageVector = Icons.Filled.Clear, contentDescription = "Clear")
                            }
                        }

                    },
                    placeholder = { Text("Search user...") }
                )
            }
            if (state.isLoading){
                item {
                    CircularProgressIndicator()
                }
            }
            items(state.filteredUser) {
                val isSelected = state.selectedUser?.id == it.id
                UserCard(
                    users = it,
                    isSelected = isSelected,
                    onClick = {
                        events(UsersEvents.OnSelectUser(it))
                    }
                )
            }
        }
        Box(
            modifier = modifier.fillMaxWidth().weight(1f)
        ) {

            if (state.selectedUser == null) {
                Text("No User selected")
            } else {
                UserInfoLayout(
                    users =  state.selectedUser,
                    state = state,
                    events = events
                )
            }
        }
    }
}


@Preview
@Composable
private fun UsersScreenPrev() {
    PawPrintsTheme {
        UsersScreen(
            state = UserState(),
            events = {},
            navController = rememberNavController()
        )
    }
}