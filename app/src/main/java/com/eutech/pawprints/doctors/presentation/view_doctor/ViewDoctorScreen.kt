package com.eutech.pawprints.doctors.presentation.view_doctor

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.chargemap.compose.numberpicker.Hours
import com.chargemap.compose.numberpicker.HoursNumberPicker
import com.eutech.pawprints.R
import com.eutech.pawprints.shared.presentation.utils.ErrorScreen
import com.eutech.pawprints.shared.presentation.utils.toast
import com.eutech.pawprints.ui.custom.LoadingScreen


@Composable
fun ViewDoctorScreen(
    modifier: Modifier = Modifier,
    id : String,
    state: ViewDoctorState,
    events: (ViewDoctorEvents) -> Unit,
    navHostController: NavHostController
) {
    val context = LocalContext.current
    LaunchedEffect(id) {
        if (id.isNotEmpty()) {
            events(ViewDoctorEvents.OnGetDoctorInfo(id))
        }
    }
    LaunchedEffect(state) {
        if (state.errors != null) {
            context.toast(state.errors)
        }
    }

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when {
            state.isLoading -> LoadingScreen(title = "Getting doctor info..")
            state.errors == null -> ErrorScreen(
                title = "${state.errors}"

            ) {
                Button(onClick = { navHostController.popBackStack()}) {
                    Text(text = "Back")
                }
            }
            else -> {
                DoctorInfoScreen(state = state, events = events, navHostController = navHostController)
            }
        }
    }
}

@Composable
fun DoctorInfoScreen(
    modifier: Modifier = Modifier,
    state: ViewDoctorState,
    events: (ViewDoctorEvents) -> Unit,
    navHostController: NavHostController
) {
    val context = LocalContext.current
    val doctors =state.doctor



    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            IconButton(onClick = {  navHostController.popBackStack() }) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
            }
        }
        Row(
            modifier = modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            LazyVerticalGrid(
                modifier = modifier
                    .fillMaxSize()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                columns = GridCells.Fixed(2)
            ) {
                item(span = { GridItemSpan(2) }) {
                    Row(
                        modifier = modifier
                            .fillMaxSize()
                            .padding(8.dp)
                    ) {
                        AsyncImage(
                            modifier = Modifier
                                .size(200.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            model = doctors?.profile,
                            contentScale = ContentScale.Crop,
                            contentDescription = doctors?.name,
                            placeholder = painterResource(R.drawable.doctor_filled),
                            error = painterResource(R.drawable.doctor_filled)
                        )
                        Column(
                            modifier = modifier
                                .padding(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(text = "${doctors?.name}", style = MaterialTheme.typography.titleLarge)
                            DoctorInfo(icon = Icons.Default.Email, info = "${doctors?.email}")
                            DoctorInfo(icon = Icons.Default.Phone, info = "${doctors?.phone}")

                            Text(text = "Tag", style = MaterialTheme.typography.titleSmall)
                            Box(
                                modifier = modifier
                                    .width(80.dp)
                                    .height(40.dp)
                                    .background(
                                        color = if (doctors?.tag != null) Color(doctors.tag) else Color.Red,
                                        shape = RoundedCornerShape(8.dp)
                                    )
                            )
                        }
                    }
                }
                item(span = { GridItemSpan(2) }) {
                    Text(text = "Scheduled Appointments", style = MaterialTheme.typography.titleLarge)
                }

            }
        }
    }
}




@Composable
fun DayPicker(
    isActive : Boolean,
    day : String,
    onClick : () -> Unit
) {
    val color = if (isActive) {
        ButtonDefaults.textButtonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        )
    } else {
        ButtonDefaults.textButtonColors()
    }
    TextButton(
        colors = color,
        onClick = onClick
    ) {
        Text(text = day)
    }

}

@Composable
fun DoctorInfo(
    modifier: Modifier = Modifier,
    icon : ImageVector,
    info : String
) {
    Row(
        modifier = modifier.padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = icon, contentDescription ="info" )
        Text(text = "${info}", style = MaterialTheme.typography.bodyLarge)

    }
}