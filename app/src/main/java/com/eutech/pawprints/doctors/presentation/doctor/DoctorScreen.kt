package com.eutech.pawprints.doctors.presentation.doctor

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.eutech.pawprints.R
import com.eutech.pawprints.doctors.data.Doctors
import com.eutech.pawprints.shared.presentation.routes.MainRouter
import com.eutech.pawprints.shared.presentation.utils.toast
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.color.ColorDialog
import com.maxkeppeler.sheets.color.models.ColorConfig
import com.maxkeppeler.sheets.color.models.ColorSelection
import com.maxkeppeler.sheets.color.models.ColorSelectionMode
import com.maxkeppeler.sheets.color.models.MultipleColors
import com.maxkeppeler.sheets.color.models.SingleColor


@Composable
fun DoctorsScreen(
    modifier: Modifier = Modifier,
     state: DoctorState,
     events: (DoctorEvents) -> Unit,
     navHostController: NavHostController
) {
    val context = LocalContext.current
    LaunchedEffect(state) {
        if (state.deleteSuccess != null) {
            context.toast(state.deleteSuccess)
        }
        if (state.errors != null) {
            context.toast(state.errors)
        }
    }
    LazyVerticalGrid(
        columns = GridCells.Fixed(count = 4),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item(span = { GridItemSpan(4) }) {
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Doctors", style = MaterialTheme.typography.titleLarge)

                Button(onClick = { navHostController.navigate(MainRouter.CreateDoctor.route) } ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.doctor),
                            contentDescription = "Add Doctor",
                            modifier = modifier.size(24.dp)
                        )
                        Text(text = "Create Doctor")
                    }
                }
            }
        }
        items(
            state.doctors, key = {it.id!!}
        ) {
            DoctorCard(
                doctors = it,
                onClick = {

                },
                onEdit={},
                onDelete= {events.invoke(DoctorEvents.OnDeleteDoctor(it))},
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DoctorCard(
    modifier: Modifier = Modifier,
    doctors: Doctors,
    onClick : () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
) {

    Card(onClick = onClick) {
        AsyncImage(
            modifier = modifier
                .fillMaxWidth()
                .height(150.dp),
            model = doctors.profile,
            contentScale = ContentScale.Crop,
            contentDescription = "${doctors.name}",
            placeholder = painterResource(R.drawable.doctor_filled),
            error = painterResource(R.drawable.doctor_filled)
        )
        ListItem(
            modifier = modifier.fillMaxWidth().padding(8.dp),
            text = {
                Text(
                    text = "${doctors.name}",
                    maxLines = 1,
                   overflow = TextOverflow.Ellipsis
                )
            },
            secondaryText = {
                Text(
                    text = "${doctors.phone}",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            },
            trailing = {
                IconButton(
                    onClick = {
                        onDelete()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        )
    }
}

@Composable
fun ViewDoctor(
    modifier: Modifier = Modifier,
    doctors: Doctors,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    modifier = Modifier
                        .width(400.dp)
                        .height(250.dp),
                    painter = painterResource(id = R.drawable.veterinarian),
                    contentDescription = "Veterinarian"
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    AsyncImage(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape),
                        model = doctors.profile,
                        contentScale = ContentScale.Crop,
                        contentDescription = doctors.name,
                        placeholder = painterResource(R.drawable.doctor_filled),
                        error = painterResource(R.drawable.doctor_filled)
                    )
                    Column {
                        Text(text = "${doctors.name}", style = MaterialTheme.typography.titleMedium)
                        Text(text = "Email : ${doctors.email}", style = MaterialTheme.typography.bodyMedium)
                        Text(text = "Phone : ${doctors.phone}", style = MaterialTheme.typography.bodyMedium)
                        Box(
                            modifier = modifier
                                .background(
                                    color = Color(doctors.tag!!),
                                    shape = RoundedCornerShape(4.dp)
                                )
                        ) {
                            Text(
                                text = "Tag",
                                color = Color.White,
                                modifier = modifier.padding(vertical = 8.dp, horizontal = 16.dp)
                            )
                        }
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    Button(
                        modifier = modifier.weight(1f),
                        shape = RoundedCornerShape(4.dp),
                        onClick = onDelete,
                        colors = ButtonDefaults
                            .buttonColors(containerColor = MaterialTheme.colorScheme.error)
                    ) {
                        Text(text = "Delete", color = Color.White, modifier = modifier.padding(4.dp))
                    }
                    Button(
                        modifier = modifier.weight(1f),
                        shape = RoundedCornerShape(4.dp),
                        onClick = onEdit,

                    ) {
                        Text(text = "Edit", modifier = modifier.padding(4.dp))
                    }
                }
            }
        }
    }
}
