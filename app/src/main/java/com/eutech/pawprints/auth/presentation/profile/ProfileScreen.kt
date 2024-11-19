package com.eutech.pawprints.auth.presentation.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.eutech.pawprints.shared.presentation.utils.ErrorScreen
import com.eutech.pawprints.ui.custom.Avatar


@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    state: ProfileState,
    events: (ProfileEvents) -> Unit,
    navHostController: NavHostController,
    onLogout : () -> Unit
) {

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when  {
            state.isLoading -> CircularProgressIndicator()
            state.errors != null -> ErrorScreen(
                title = state.errors
            ) {
               Button(
                   modifier = modifier.padding(12.dp),
                   onClick = { /*TODO*/ }
               ) {
                   Text(text = "Back")
               } 
            } else -> {
                val admin = state.administrator
                Card(
                    modifier = modifier.padding(16.dp)
                ) {
                    Column(
                        modifier = modifier
                            .fillMaxSize(0.5f)
                            .padding(16.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Avatar(image = admin?.profile ?: "")
                        Text(
                            text = admin?.name ?: "No name",
                            style = MaterialTheme.typography.titleLarge
                        )
                        Text(
                            text = admin?.email ?: "No email",
                            style = MaterialTheme.typography.labelMedium,
                            color = Color.Gray
                        )
                        Text(
                            text = admin?.phone ?: "No phone",
                            style = MaterialTheme.typography.labelMedium,
                            color = Color.Gray
                        )
                        Row(
                            modifier = modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = { /*TODO*/ },
                                modifier = modifier.weight(1f),
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.Green,
                                    contentColor = Color.Black
                                )
                            ) {
                                Text(text = "Edit Profile")
                            }
                            Button(
                                onClick =  onLogout,
                                modifier = modifier.weight(1f),
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.errorContainer,
                                    contentColor = MaterialTheme.colorScheme.onErrorContainer
                                )
                            ) {
                                Text(text = "Logout")
                            }
                        }
                    }
                }
            }
        }
    }
}