package com.eutech.pawprints.pets.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.eutech.pawprints.appointments.data.appointment.Appointments
import com.eutech.pawprints.appointments.presentation.appointment.PetAppointmentCard
import com.eutech.pawprints.pets.PetState


@Composable
fun PetAppointmentsLayout(
    modifier: Modifier = Modifier,
    isLoading : Boolean,
    appointments : List<Appointments>,
    error : String ? = null,
) {
    when {
        isLoading -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        appointments.isEmpty() && !isLoading ->  Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("No Appointment Yet")
        }
        error != null -> Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(error)
        } else -> {
        LazyColumn {
            items(appointments) {
                PetAppointmentCard(appointments = it)
            }
        }
    }
    }
}