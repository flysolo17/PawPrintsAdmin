package com.eutech.pawprints.home.presentation.components

import android.icu.text.CaseMap.Title
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.PointOfSale
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.eutech.pawprints.appointments.data.appointment.AppointmentWithAttendeesAndPets
import com.eutech.pawprints.appointments.data.appointment.Appointments
import com.eutech.pawprints.appointments.data.appointment.displayTime
import com.eutech.pawprints.appointments.data.appointment.getColor
import com.eutech.pawprints.appointments.presentation.appointment.AppointmentEvent
import com.eutech.pawprints.appointments.presentation.appointment.DisplayDate
import com.eutech.pawprints.home.presentation.HomeEvents
import com.eutech.pawprints.home.presentation.HomeState
import com.eutech.pawprints.shared.presentation.utils.toast
import com.eutech.pawprints.ui.custom.GroupAvatars
import com.eutech.pawprints.ui.custom.Header
import com.eutech.pawprints.ui.custom.Label
import com.eutech.pawprints.ui.custom.SubHeader


@Composable
fun AppointmentLayout(
    modifier: Modifier = Modifier,
    isLoading : Boolean ,
    appointments: List<AppointmentWithAttendeesAndPets>,
    onFilterChange: () -> Unit,
    state : HomeState,
    events: (HomeEvents) -> Unit
 ) {
    val context  = LocalContext.current
    LazyColumn(
        modifier = modifier.fillMaxSize().padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        item {
            DashboardInfoCard(
                modifier = modifier.fillMaxWidth(),
                icon = Icons.Filled.ShoppingCart,
                label = "Orders",
                data = "0",
                iconBackGround = Color(0xFF2E7D32),
            ) { }
        }
        item {
            DashboardInfoCard(
                modifier = modifier.fillMaxWidth(),
                icon = Icons.Filled.PointOfSale,
                label = "Point of Sale",
                data = "0",
                iconBackGround = Color(0xFF2E7D32),
            ) { }
        }
        item {
            DashboardInfoCard(
                modifier = modifier.fillMaxWidth(),
                icon = Icons.Filled.CalendarMonth,
                label = "Appointments",
                data = state.appointment.size.toString(),
                iconBackGround = Color(0xFF2E7D32),
            ) { }
        }
        item {
            Text(
                text = if (isLoading ) "Loading..." else "Appointments",
                style = MaterialTheme.typography.titleLarge.copy(
                    color = MaterialTheme.colorScheme.onSurface
                ),
                modifier = modifier.padding(
                    bottom = 8.dp
                )
            )
        }
        items(appointments) {
            AppointmentCard(data = it, isLoading = state.updatingStatus, onUpdateStatus = { s, a ->
                events.invoke(HomeEvents.OnUpdateAppointment(s,a))
                context.toast("test")
            })
        }
    }
}


