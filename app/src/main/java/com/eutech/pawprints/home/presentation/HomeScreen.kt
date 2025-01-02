package com.eutech.pawprints.home.presentation

import android.os.Build
import android.widget.CalendarView
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
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
import androidx.compose.ui.draw.clip
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
import com.eutech.pawprints.R
import com.eutech.pawprints.appointments.data.appointment.AppointmentStatus
import com.eutech.pawprints.appointments.data.appointment.Appointments
import com.eutech.pawprints.appointments.data.appointment.getFormattedDate
import com.eutech.pawprints.appointments.presentation.components.PawPrintCalendarView
import com.eutech.pawprints.home.presentation.components.AppointmentLayout
import com.eutech.pawprints.home.presentation.components.DashboardInfoCard
import com.eutech.pawprints.schedule.data.countSlots
import com.eutech.pawprints.shared.presentation.routes.MainRouter
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

    LaunchedEffect(state.selectedDate) {
        events(HomeEvents.OnGetSchedules(state.selectedDate))
    }

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

//
//    if (pastDueAppointments.isNotEmpty()) {
//        events(HomeEvents.OnAutoCancelAppointments(pastDueAppointments))
//    }



    Row(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .weight(1f)
        ) {
            Column {
                Row(
                    modifier = modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    ListItem(
                        leadingContent = {
                            Image(
                                painter = painterResource(R.drawable.logo),
                                contentDescription = "Logo",
                                modifier = modifier.size(32.dp).clip(CircleShape),

                                )
                        },
                        headlineContent = {
                            Text("Welcome to Aucena Veterinary App")
                        },
                        supportingContent = {
                            Text("Clinic Hours: Mon - Fri 9:00 AM- 5:30 PM,Sat - Appointment Only", style = MaterialTheme.typography.labelMedium)
                        }
                    )
                }
            Card(modifier = modifier.fillMaxSize().padding(8.dp)) {
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
                        events(HomeEvents.ChangeSelectedMonth(it))
                    }
                )
                }

            }
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
                events = events,
                onClickOrder = {
                    navHostController.navigate(MainRouter.Orders.route)
                },
                orders = state.orders,
                onNavigateToPos = {
                    navHostController.navigate(MainRouter.Pos.route)
                },
                onNavigateToAppointments = {
                    navHostController.navigate(MainRouter.Appointments.route)
                }
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


