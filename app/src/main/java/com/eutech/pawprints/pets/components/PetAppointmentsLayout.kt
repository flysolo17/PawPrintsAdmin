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
import com.eutech.pawprints.appointments.presentation.appointment.PetAppointmentCard
import com.eutech.pawprints.pets.PetState


@Composable
fun PetAppointmentsLayout(
    modifier: Modifier = Modifier,
    state : PetState,
) {
    when {
        state.isGettingPetSchedule -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        state.selectedPetAppointments.isEmpty() ->  Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("No Appointment Yet")
        }
        state.petAppointmentError != null -> Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(state.petAppointmentError)
        } else -> {
        LazyColumn {
            items(state.selectedPetAppointments) {
                PetAppointmentCard(appointments = it)
            }
        }
    }
    }
}