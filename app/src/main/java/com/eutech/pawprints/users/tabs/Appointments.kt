package com.eutech.pawprints.users.tabs

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.eutech.pawprints.shared.presentation.utils.ErrorScreen
import com.eutech.pawprints.users.AppointmentTabState
import com.eutech.pawprints.users.components.AppointmentTabCard
import com.eutech.pawprints.users.components.InboxCard


@Composable
fun AppointmentsTab(
    modifier: Modifier = Modifier,
    appointmentTabState: AppointmentTabState
) {
    LazyColumn(
        modifier = modifier.fillMaxSize()
    ) {
        if (appointmentTabState.isLoading) {
            item {
                LinearProgressIndicator(
                    modifier = modifier.fillMaxWidth()
                )
            }
        }
        if(appointmentTabState.errors != null) {
            item {
                Box(
                    modifier = modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    ErrorScreen(
                        title = appointmentTabState.errors
                    ) {
                    }
                }
            }
        }

        if (
            appointmentTabState.errors == null
            &&
            appointmentTabState.appointments.isEmpty() &&
            !appointmentTabState.isLoading
            ) {
            item {
                Box(
                    modifier = modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("no appointments yet!")
                }
            }
        }
        items(appointmentTabState.appointments) {
            AppointmentTabCard(
                appointments = it
            )
        }
    }
}