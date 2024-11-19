package com.eutech.pawprints.home.presentation

import android.os.Build
import android.widget.CalendarView
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column


import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalMapOf
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.os.trace
import androidx.navigation.NavHostController
import com.eutech.pawprints.appointments.data.appointment.AppointmentStatus
import com.eutech.pawprints.appointments.data.appointment.Appointments
import com.eutech.pawprints.appointments.data.appointment.getFormattedDate
import com.eutech.pawprints.appointments.presentation.components.PawPrintCalendarView
import com.eutech.pawprints.home.presentation.components.AppointmentLayout
import com.eutech.pawprints.home.presentation.components.DashboardInfoCard
import com.eutech.pawprints.schedule.data.countSlots
import com.eutech.pawprints.shared.presentation.utils.toast
import org.threeten.bp.LocalDate
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    state : HomeState,
    events : (HomeEvents) -> Unit,
    navHostController: NavHostController
) {
    val context = LocalContext.current


    val currentDate = remember { Date() }

    // Filter appointments based on whether the date is past due
    val pastDueAppointments by remember(state.appointment) {
        mutableStateOf(
            state.appointment.filter { appointment ->
                val date = appointment.appointments.getFormattedDate()
                date?.before(currentDate) ?: false
            }
        )
    }

    LaunchedEffect(state.errors, state.messages) {
        state.errors?.let { context.toast(it) }
        state.messages?.let { context.toast(it) }
    }


    if (pastDueAppointments.isNotEmpty()) {
        events(HomeEvents.OnAutoCancelAppointments(pastDueAppointments))
    }
    Row(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .weight(1f)
        ) {
            DashboardInfo(
                modifier = modifier,
                state
            )
            val schedules = state.schedules.map {it.schedule!!}
            PawPrintCalendarView(
                isLoading = state.isLoading,
                selectedDate = state.selectedDate,
                schedules = schedules,
                appointments = emptyList(),
                onDateSelected = {
                    events(HomeEvents.OnDateChange(it))
                },
                onMonthChange = {
                    events(HomeEvents.OnGetSchedules(it))
                }
            )
        }

        Box(
            modifier = modifier
                .fillMaxSize()
                .weight(0.4f)
        ) {
            AppointmentLayout(
                modifier = modifier,
                isLoading = state.isGettingAppointments,
                appointments = state.appointment,
                onFilterChange = {},
                state = state,
                events = events
            )
        }
    }

}



@Composable
fun DashboardInfo(
    modifier: Modifier = Modifier,
    state : HomeState
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        DashboardInfoCard(
            modifier = modifier.fillMaxWidth().weight(1f),
            icon = Icons.Filled.Schedule,
            label = "Available Slots",
            data = state.schedules.map { it.schedule!! }.countSlots(state.selectedDate).toString(),
            iconBackGround = Color(0xFF2E7D32),
        ) { }

        DashboardInfoCard(
            modifier = modifier.fillMaxWidth().weight(1f),
            icon = Icons.Filled.Checklist,
            label = "Pending",
            data = state.appointment.filter { it.appointments.status == AppointmentStatus.PENDING }.size.toString(),
            iconBackGround = Color(0xFFFFEB3B),
            hasButton = true
        ) { }

        DashboardInfoCard(
            modifier = modifier.fillMaxWidth().weight(1f),
            icon = Icons.Filled.Checklist,
            label = "Confirmed",
            data = state.appointment.filter { it.appointments.status == AppointmentStatus.CONFIRMED }.size.toString(),
            iconBackGround = Color(0xFF2E7D32),
            hasButton = true
        ) { }
    }





}


